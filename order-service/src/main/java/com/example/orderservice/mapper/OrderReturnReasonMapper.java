package com.example.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orderservice.po.OrderReturnReason;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 退货原因 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface OrderReturnReasonMapper extends BaseMapper<OrderReturnReason> {

}
