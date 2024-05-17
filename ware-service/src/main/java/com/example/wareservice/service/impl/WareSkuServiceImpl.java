package com.example.wareservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.dto.SkuHasStockDTO;
import com.example.common.util.PageUtils;
import com.example.wareservice.mapper.WareSkuMapper;
import com.example.wareservice.po.WareSku;
import com.example.wareservice.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品库存 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class WareSkuServiceImpl extends ServiceImpl<WareSkuMapper, WareSku> implements WareSkuService {
    @Autowired
    private WareSkuMapper wareSkuMapper;

    @Override
    public Page<WareSku> queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSku> queryWrapper = new LambdaQueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            queryWrapper.eq(WareSku::getSkuId, skuId);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq(WareSku::getWareId, wareId);
        }
        return this.page(PageUtils.getPage(params), queryWrapper);
    }

    @Override
    public List<SkuHasStockDTO> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockDTO> collect = skuIds.stream().map(skuId -> {
            SkuHasStockDTO skuHasStockDTO = new SkuHasStockDTO();
            //查询当前总库存量
            Long count = wareSkuMapper.getSkuStock(skuId);
            skuHasStockDTO.setSkuId(skuId);
            //count==null?false:count>0 = count==null?false:&&count>0
            skuHasStockDTO.setHasStock(count == null ? false : count > 0);
            return skuHasStockDTO;
        }).collect(Collectors.toList());
        return collect;
    }
}
