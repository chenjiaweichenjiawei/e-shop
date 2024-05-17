package com.example.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.po.AttrAttrgroupRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 属性&属性分组关联 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-03-03
 */
@Mapper
public interface AttrAttrgroupRelationMapper extends BaseMapper<AttrAttrgroupRelation> {
    /**
     * 批量删除属性和分组的关联关系
     *
     * @param vos
     */
    void deleteBatchRelation(@Param("vos") List<AttrAttrgroupRelation> vos);
}
