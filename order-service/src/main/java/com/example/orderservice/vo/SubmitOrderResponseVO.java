package com.example.orderservice.vo;

import com.example.orderservice.po.Order;
import lombok.Data;

@Data
public class SubmitOrderResponseVO {

    private Order order;
    private Integer code;//0成功


}
