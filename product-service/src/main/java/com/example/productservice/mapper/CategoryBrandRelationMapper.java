package com.example.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.po.CategoryBrandRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 品牌分类关联 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-02-27
 */
@Mapper
public interface CategoryBrandRelationMapper extends BaseMapper<CategoryBrandRelation> {
    /**
     * 修改关联关系品牌信息
     *
     * @param brandId
     * @param name
     */
    @Update("update pms_category_brand_relation set brand_name=#{name} where brand_id=#{brandId};")
    void updateBrandName(@Param("brandId") Long brandId, @Param("name") String name);

    /**
     * 修改关联关系分类信息
     *
     * @param catId
     * @param name
     */
    @Update("update pms_category_brand_relation set catelog_name=#{name} where catelog_id=#{catId};")
    void updateCategoryName(@Param("catId") Long catId, @Param("name") String name);
}
