package com.example.orderservice.vo;


import lombok.Data;

@Data
public class SkuStockVO {
    private Long skuId;
    private Boolean hasStock;
}
