package com.example.couponservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.couponservice.po.CouponSpuRelation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 优惠券与产品关联 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Mapper
public interface CouponSpuRelationMapper extends BaseMapper<CouponSpuRelation> {

}
