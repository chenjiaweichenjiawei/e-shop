package com.example.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.po.SpuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * spu信息 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Mapper
public interface SpuInfoMapper extends BaseMapper<SpuInfo> {
    /**
     * 更改spu上架状态
     *
     * @param spuId
     * @param code
     */
    void updateSpuStatus(@Param("spuId") Long spuId, @Param("code") int code);
}
