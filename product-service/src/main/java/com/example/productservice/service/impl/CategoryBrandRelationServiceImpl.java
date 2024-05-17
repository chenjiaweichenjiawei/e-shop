package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.BrandMapper;
import com.example.productservice.mapper.CategoryBrandRelationMapper;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.po.Brand;
import com.example.productservice.po.CategoryBrandRelation;
import com.example.productservice.service.BrandService;
import com.example.productservice.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 品牌分类关联 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-02-27
 */
@Service
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationMapper, CategoryBrandRelation> implements CategoryBrandRelationService {
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private CategoryBrandRelationMapper categoryBrandRelationMapper;
    @Autowired
    private BrandService brandService;

    @Override
    public void saveDetail(CategoryBrandRelation categoryBrandRelation) {
        String brandName = brandMapper.selectById(categoryBrandRelation.getBrandId()).getName();
        String categoryName = categoryMapper.selectById(categoryBrandRelation.getCatelogId()).getName();
        categoryBrandRelation.setBrandName(brandName);
        categoryBrandRelation.setCatelogName(categoryName);
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrand(Long brandId, String name) {
        categoryBrandRelationMapper.updateBrandName(brandId, name);
    }

    @Override
    public void updateCategory(Long catId, String name) {
        categoryBrandRelationMapper.updateCategoryName(catId, name);
    }

    @Override
    public List<Brand> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelation> catelogId = categoryBrandRelationMapper.selectList(
                new LambdaQueryWrapper<CategoryBrandRelation>()
                        .eq(CategoryBrandRelation::getCatelogId, catId));
        return catelogId.stream().map(item -> {
            Long brandId = item.getBrandId();
            return brandService.getById(brandId);
        }).collect(Collectors.toList());
    }
}
