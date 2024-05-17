package com.example.orderservice.dto;

import com.example.orderservice.po.Order;
import com.example.orderservice.po.OrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderCreateDTO {
    private Order order;
    private List<OrderItem> items;
    private BigDecimal payPrice;
    private BigDecimal fare;
}
