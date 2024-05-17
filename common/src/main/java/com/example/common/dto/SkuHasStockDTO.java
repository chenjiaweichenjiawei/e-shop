package com.example.common.dto;

import lombok.Data;

@Data
public class SkuHasStockDTO {
    private Long skuId;
    private Boolean hasStock;
}
