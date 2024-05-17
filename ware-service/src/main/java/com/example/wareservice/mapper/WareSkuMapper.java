package com.example.wareservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wareservice.po.WareSku;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品库存 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Mapper
public interface WareSkuMapper extends BaseMapper<WareSku> {
    /**
     * 查询sku的库存信息
     *
     * @param skuId
     * @return
     */
    Long getSkuStock(@Param("skuId") Long skuId);
}
