package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.util.PageUtils;
import com.example.productservice.mapper.SkuInfoMapper;
import com.example.productservice.po.SkuImages;
import com.example.productservice.po.SkuInfo;
import com.example.productservice.po.SpuInfoDesc;
import com.example.productservice.service.*;
import com.example.productservice.vo.skuItemvo.SkuItemSaleAttrsVO;
import com.example.productservice.vo.skuItemvo.SkuItemVO;
import com.example.productservice.vo.skuItemvo.SpuItemAttrGroupVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * sku信息 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public Page<SkuInfo> queryPageByParams(Map<String, Object> params) {
        LambdaQueryWrapper<SkuInfo> queryWrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((w) -> {
                w.eq(SkuInfo::getSkuId, key).or().like(SkuInfo::getSkuName, key);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq(SkuInfo::getCatalogId, catelogId);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq(SkuInfo::getBrandId, brandId);
        }
        String max = (String) params.get("max");
        if (!StringUtils.isEmpty(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(new BigDecimal("0")) > 0) {
                    queryWrapper.le(SkuInfo::getPrice, max);
                }
            } catch (Exception ignored) {
            }
        }
        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            queryWrapper.ge(SkuInfo::getPrice, min);
        }
        return this.page(PageUtils.getPage(params), queryWrapper);
    }

    @Override
    public List<SkuInfo> getSkusBySpuId(Long spuId) {
        return this.list(
                new LambdaQueryWrapper<SkuInfo>().eq(SkuInfo::getSpuId, spuId));
    }

    @Override
    public SkuItemVO getSkuItem(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVO skuItemVO = new SkuItemVO();

        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            //1、sku基本信息 pms_sku_info
            SkuInfo skuInfo = this.getById(skuId);
            skuItemVO.setInfo(skuInfo);
            return skuInfo;
        }, threadPoolExecutor);

        CompletableFuture<Void> saleAttrFuture = skuInfoFuture.thenAcceptAsync((skuInfo) -> {
            //3、spu的销售属性组合
            List<SkuItemSaleAttrsVO> skuItemSaleAttrsVOS = skuSaleAttrValueService.getSaleAttrsBySpuId(skuInfo.getSpuId());
            skuItemVO.setSaleAttr(skuItemSaleAttrsVOS);
        }, threadPoolExecutor);

        CompletableFuture<Void> despFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            //4、spu的介绍
            SpuInfoDesc spuInfoDesc = spuInfoDescService.getBySpuId(skuInfo.getSpuId());
            skuItemVO.setDesp(spuInfoDesc);
        }, threadPoolExecutor);

        CompletableFuture<Void> groupAttrsFuture = skuInfoFuture.thenAcceptAsync(skuInfo -> {
            //5、spu的规格参数信息
            List<SpuItemAttrGroupVO> attrGroupVOS = attrGroupService
                    .getAttrGroupWithAttrsBySpuId(skuInfo.getSpuId(), skuInfo.getCatalogId());
            skuItemVO.setGroupAttrs(attrGroupVOS);
        }, threadPoolExecutor);

        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            //2、sku图片信息 pms_sku_images
            List<SkuImages> skuImagesList = skuImagesService.getImagesBySkuId(skuId);
            skuItemVO.setImages(skuImagesList);
        }, threadPoolExecutor);

        CompletableFuture.allOf(saleAttrFuture, despFuture, imagesFuture, groupAttrsFuture)
                //调用get()进行等待全部完成
                .get();
        return skuItemVO;
    }
}
