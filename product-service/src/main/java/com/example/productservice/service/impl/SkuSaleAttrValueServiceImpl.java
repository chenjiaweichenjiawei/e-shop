package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.SkuSaleAttrValueMapper;
import com.example.productservice.po.SkuSaleAttrValue;
import com.example.productservice.service.SkuSaleAttrValueService;
import com.example.productservice.vo.skuItemvo.SkuItemSaleAttrsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * sku销售属性&值 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueMapper, SkuSaleAttrValue> implements SkuSaleAttrValueService {
    @Autowired
    private SkuSaleAttrValueMapper skuSaleAttrValueMapper;

    @Override
    public List<SkuItemSaleAttrsVO> getSaleAttrsBySpuId(Long spuId) {
        return skuSaleAttrValueMapper.getSaleAttrsBySpuId(spuId);
    }

    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {
        return skuSaleAttrValueMapper.getSkuSaleAttrValuesAsStringList(skuId);
    }
}
