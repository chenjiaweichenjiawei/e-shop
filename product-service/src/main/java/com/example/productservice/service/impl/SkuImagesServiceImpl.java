package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.SkuImagesMapper;
import com.example.productservice.po.SkuImages;
import com.example.productservice.service.SkuImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * sku图片 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesMapper, SkuImages> implements SkuImagesService {
    @Autowired
    private SkuImagesMapper skuImagesMapper;

    @Override
    public List<SkuImages> getImagesBySkuId(Long skuId) {
        return baseMapper.selectList(
                new LambdaQueryWrapper<SkuImages>()
                        .eq(SkuImages::getSkuId, skuId));
    }
}
