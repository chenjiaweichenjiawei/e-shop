package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.Brand;
import com.example.productservice.po.CategoryBrandRelation;

import java.util.List;

/**
 * <p>
 * 品牌分类关联 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-02-27
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelation> {
    /**
     * 新增品牌与分类的关联关系
     *
     * @param categoryBrandRelation
     */
    void saveDetail(CategoryBrandRelation categoryBrandRelation);

    /**
     * 修改关联关系的品牌名称
     *
     * @param brandId 品牌id
     * @param name    新名称
     */
    void updateBrand(Long brandId, String name);

    /**
     * 修改关联关系的分类名称
     *
     * @param catId 分类id
     * @param name  新名称
     */
    void updateCategory(Long catId, String name);

    /**
     * 获取分类的品牌集合
     *
     * @param catId
     * @return
     */
    List<Brand> getBrandsByCatId(Long catId);
}
