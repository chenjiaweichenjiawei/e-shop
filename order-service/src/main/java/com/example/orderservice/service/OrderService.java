package com.example.orderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.orderservice.po.Order;
import com.example.orderservice.vo.MemberRespVO;
import com.example.orderservice.vo.OrderConfirmVO;
import com.example.orderservice.vo.OrderSubmitVO;
import com.example.orderservice.vo.SubmitOrderResponseVO;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
public interface OrderService extends IService<Order> {
    /**
     * 订单确认
     *
     * @param orderConfirmVO
     * @param memberRespVO
     * @return
     */
    OrderConfirmVO confirm(OrderConfirmVO orderConfirmVO, MemberRespVO memberRespVO);

    /**
     * 订单提交
     *
     * @param vo
     * @param memberRespVO
     * @return
     */
    SubmitOrderResponseVO submitOrder(OrderSubmitVO vo, MemberRespVO memberRespVO);
}
