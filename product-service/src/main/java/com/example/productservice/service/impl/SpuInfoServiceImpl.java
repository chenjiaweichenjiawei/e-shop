package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.ProductConstant;
import com.example.common.constant.StatusCode;
import com.example.common.dto.SkuHasStockDTO;
import com.example.common.dto.SkuReductionDTO;
import com.example.common.dto.SpuBoundDTO;
import com.example.common.dto.es.SkuEsModel;
import com.example.common.util.PageUtils;
import com.example.common.util.Result;
import com.example.productservice.feign.CouponServiceFeignClient;
import com.example.productservice.feign.SearchServiceFeignClient;
import com.example.productservice.feign.WareServiceFeignClient;
import com.example.productservice.mapper.SpuInfoMapper;
import com.example.productservice.po.*;
import com.example.productservice.service.*;
import com.example.productservice.vo.spusavevo.Attr;
import com.example.productservice.vo.spusavevo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * spu信息 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Slf4j
@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo> implements SpuInfoService {
    @Autowired
    private SpuInfoMapper spuInfoMapper;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private SpuImagesService spuImagesService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private CouponServiceFeignClient couponServiceFeignClient;
    @Autowired
    private WareServiceFeignClient wareServiceFeignClient;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SearchServiceFeignClient searchServiceFeignClient;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSpuInfo(SpuSaveVO vo) {
        //1.保存spu基本信息；pms_spu_info
        SpuInfo spuInfo = new SpuInfo();
        BeanUtils.copyProperties(vo, spuInfo);
        this.save(spuInfo);
        //2.保存spu的描述信息
        List<String> decript = vo.getDecript();
        SpuInfoDesc spuInfoDesc = new SpuInfoDesc();
        spuInfoDesc.setSpuId(spuInfo.getId());
        spuInfoDesc.setDecript(String.join(",", decript));
        spuInfoDescService.save(spuInfoDesc);
        //3.保存spu的图片集
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfo.getId(), images);
        //4.保存spu的规格参数;pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValue> collect = baseAttrs.stream().map(item -> {
            ProductAttrValue productAttrValue = new ProductAttrValue();
            productAttrValue.setAttrId(item.getAttrId());
            com.example.productservice.po.Attr attr = attrService.getById(item.getAttrId());
            productAttrValue.setAttrName(attr.getAttrName());
            productAttrValue.setAttrValue(item.getAttrValues());
            productAttrValue.setQuickShow(item.getShowDesc());
            productAttrValue.setSpuId(spuInfo.getId());
            return productAttrValue;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(collect);


        //5.保存spu的积分信息(跨服务)
        Bounds bounds = vo.getBounds();
        SpuBoundDTO spuBoundDTO = new SpuBoundDTO();
        BeanUtils.copyProperties(bounds, spuBoundDTO);
        spuBoundDTO.setSpuId(spuInfo.getId());
        Result<Void> result = couponServiceFeignClient.saveBounds(spuBoundDTO);
        if (StatusCode.ERROR.getCode().equals(result.getCode())) {
            log.error("远程保存spu优惠信息异常");
        }


        //6.保存当前spu对应的所有sku信息；
        //6.1sku的基本信息;pms_sku_info
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                String defaultImg = "";
                for (Images i : item.getImages()) {
                    if (i.getDefaultImg() == 1) {
                        defaultImg = i.getImgUrl();
                    }
                }
                SkuInfo skuInfo = new SkuInfo();
                BeanUtils.copyProperties(item, skuInfo);
                //添加vo中没有的信息
                skuInfo.setBrandId(spuInfo.getBrandId());
                skuInfo.setCatalogId(spuInfo.getCatalogId());
                skuInfo.setSaleCount(0L);
                skuInfo.setSpuId(spuInfo.getId());
                skuInfo.setSkuDefaultImg(defaultImg);
                skuInfoService.save(skuInfo);
                //6.2保存sku图片信息;pms_sku_images
                Long skuId = skuInfo.getSkuId();
                List<SkuImages> imgs = item.getImages().stream().map(img -> {
                    SkuImages skuImages = new SkuImages();
                    skuImages.setSkuId(skuId);
                    skuImages.setImgUrl(img.getImgUrl());
                    skuImages.setDefaultImg(img.getDefaultImg());
                    return skuImages;
                }).filter(entity -> !StringUtils.isEmpty(entity.getImgUrl())).collect(Collectors.toList());
                skuImagesService.saveBatch(imgs);
                //6.3保存sku的销售属性;pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValue> skuSaleAttrValueList = attr.stream().map(obj -> {
                    SkuSaleAttrValue skuSaleAttrValue = new SkuSaleAttrValue();
                    BeanUtils.copyProperties(obj, skuSaleAttrValue);
                    skuSaleAttrValue.setSkuId(skuId);
                    return skuSaleAttrValue;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);

                //6.4sku的优惠满减信息(跨服务);
                SkuReductionDTO skuReductionDTO = new SkuReductionDTO();
                BeanUtils.copyProperties(item, skuReductionDTO);
                skuReductionDTO.setSkuId(skuId);
                if (skuReductionDTO.getFullCount() > 0 || skuReductionDTO.getFullPrice().compareTo(new BigDecimal("0")) > 0) {
                    Result<Void> result1 = couponServiceFeignClient.saveFullReduction(skuReductionDTO);
                    if (StatusCode.ERROR.getCode().equals(result1.getCode())) {
                        log.error("远程保存spu满减信息异常");
                    }
                }
            });
        }
    }

    @Override
    public Page<SpuInfo> queryPageByParams(Map<String, Object> params) {
        LambdaQueryWrapper<SpuInfo> queryWrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            //等价sql: status=1 and (id=1 or spu_name like xxx)
            queryWrapper.and((wrapper) -> {
                wrapper.eq(SpuInfo::getId, key).or().like(SpuInfo::getSpuName, key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq(SpuInfo::getPublishStatus, status);
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq(SpuInfo::getBrandId, brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq(SpuInfo::getCatalogId, catelogId);
        }
        return this.page(PageUtils.getPage(params), queryWrapper);
    }

    @Override
    public void up(Long spuId) throws IOException {
        //查出当前spu对应的所有sku信息,品牌的名字
        List<SkuInfo> skuInfoList = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfo::getSkuId).collect(Collectors.toList());
        //当前spu所有可以被用来检索的规格属性
        List<ProductAttrValue> baseAttrs = productAttrValueService.baseAttrListForSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(ProductAttrValue::getAttrId).collect(Collectors.toList());
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIds);
        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(item -> searchAttrIds.contains(item.getAttrId())).map(item -> {
            //转换成es模型中属性对象
            SkuEsModel.Attrs attrs1 = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attrs1);
            return attrs1;
        }).collect(Collectors.toList());
        //发送远程调用,查是否有库存;
        Map<Long, Boolean> stockMap = null;
        try {
            Result<List<SkuHasStockDTO>> skuHasStockResult = wareServiceFeignClient.getSkuHasStock(skuIdList);
            stockMap = skuHasStockResult.getData().stream().collect(Collectors.toMap(SkuHasStockDTO::getSkuId, SkuHasStockDTO::getHasStock));
        } catch (Exception e) {
            log.info("库存服务查异常{}", e);
        }
        //封装数据
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skuInfoList.stream().map(sku -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(sku, skuEsModel);
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());
            if (finalStockMap == null) {
                skuEsModel.setHasStock(false);
            } else {
                skuEsModel.setHasStock(finalStockMap.get(sku.getSkuId()));
            }
            //热度评分数据
            skuEsModel.setHotScore(0L);
            Brand brand = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());
            Category category = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(category.getName());
            //设置检索属性
            skuEsModel.setAttrs(attrsList);
            return skuEsModel;
        }).collect(Collectors.toList());
        //发送至es进行保存
        Result<Void> result = searchServiceFeignClient.productStatuesUp(upProducts);
        if (StatusCode.ERROR.getCode().equals(result.getCode())) {
            //修改spu状态
            spuInfoMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        } else {
            //失败
            //接口幂等性：重试机制

            /**Feign调用机制
             1.构造请求数据,将对象转换为json
             2.发送请求执行
             3.执行请求会有重试机制
             *
             */
        }
    }

    @Override
    public SpuInfo getSpuInfoBySkuId(Long skuId) {
        SkuInfo byId = skuInfoService.getById(skuId);
        Long spuId = byId.getSpuId();
        return getById(spuId);
    }
}
