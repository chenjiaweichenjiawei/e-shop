package com.example.couponservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.couponservice.po.Coupon;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 优惠券信息 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Mapper
public interface CouponMapper extends BaseMapper<Coupon> {

}
