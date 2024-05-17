package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.BrandMapper;
import com.example.productservice.mapper.CategoryBrandRelationMapper;
import com.example.productservice.po.Brand;
import com.example.productservice.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 品牌 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2023-11-14
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {
    @Autowired
    private CategoryBrandRelationMapper categoryBrandRelationMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDetail(Brand brand) {
        this.updateById(brand);
        if (brand.getName() != null) {
            //更新数据库冗余字段冗余字段
            categoryBrandRelationMapper.updateBrandName(brand.getBrandId(), brand.getName());
        }
    }
}
