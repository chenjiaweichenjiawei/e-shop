package com.example.couponservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.couponservice.po.CouponHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 优惠券领取历史记录 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Mapper
public interface CouponHistoryMapper extends BaseMapper<CouponHistory> {

}
