package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.ProductAttrValueMapper;
import com.example.productservice.po.ProductAttrValue;
import com.example.productservice.service.ProductAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * spu属性值 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueMapper, ProductAttrValue> implements ProductAttrValueService {

    @Override
    public List<ProductAttrValue> baseAttrListForSpu(Long spuId) {
        return this.baseMapper.selectList(
                new LambdaQueryWrapper<ProductAttrValue>().eq(ProductAttrValue::getSpuId, spuId));
    }
}
