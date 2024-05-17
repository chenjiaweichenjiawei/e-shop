package com.example.wareservice.vo;

import lombok.Data;

import java.util.List;

@Data
public class WareSkuLockVO {
    private String orderSn;
    private List<OrderItemVO> locks;

}
