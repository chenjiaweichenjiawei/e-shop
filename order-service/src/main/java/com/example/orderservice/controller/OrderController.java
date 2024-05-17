package com.example.orderservice.controller;


import com.example.common.util.Result;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.MemberRespVO;
import com.example.orderservice.vo.OrderConfirmVO;
import com.example.orderservice.vo.OrderSubmitVO;
import com.example.orderservice.vo.SubmitOrderResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/confirm")
    public Result<OrderConfirmVO> confirm(@RequestBody OrderConfirmVO orderConfirmVO, @RequestBody MemberRespVO memberRespVO) {
        OrderConfirmVO vo = orderService.confirm(orderConfirmVO, memberRespVO);
        return Result.success(vo);
    }

    @PostMapping("/submitOrder")
    public Result<SubmitOrderResponseVO> submitOrder(@RequestBody OrderSubmitVO orderSubmitVO, @RequestBody MemberRespVO memberRespVO) {
        //try {
        SubmitOrderResponseVO responseVo = orderService.submitOrder(orderSubmitVO, memberRespVO);
        if (responseVo.getCode() == 0) {
            return Result.success(responseVo);
        } else {
            String msg = "下单失败";
            switch (responseVo.getCode()) {
                case 1:
                    msg += "订单信息过期，请刷新重新提交";
                    break;
                case 2:
                    msg += "订单商品价格发生变化，请确认后再次提交";
                    break;
                case 3:
                    msg += "商品库存不足";
            }
            return Result.error(msg);
        }
    }
}
