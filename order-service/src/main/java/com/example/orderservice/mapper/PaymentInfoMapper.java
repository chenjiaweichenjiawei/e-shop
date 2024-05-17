package com.example.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orderservice.po.PaymentInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 支付信息表 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface PaymentInfoMapper extends BaseMapper<PaymentInfo> {

}
