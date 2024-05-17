package com.example.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orderservice.po.OrderReturnApply;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单退货申请 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface OrderReturnApplyMapper extends BaseMapper<OrderReturnApply> {

}
