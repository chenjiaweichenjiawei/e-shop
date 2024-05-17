package com.example.couponservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.couponservice.po.MemberPrice;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品会员价格 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Mapper
public interface MemberPriceMapper extends BaseMapper<MemberPrice> {

}
