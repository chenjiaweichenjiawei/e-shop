package com.example.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.po.AttrGroup;
import com.example.productservice.vo.skuItemvo.SpuItemAttrGroupVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 属性分组 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2023-11-21
 */
@Mapper
public interface AttrGroupMapper extends BaseMapper<AttrGroup> {
    /**
     * 获取spu规格参数
     *
     * @param spuId
     * @param catalogId
     * @return
     */
    List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuId(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
