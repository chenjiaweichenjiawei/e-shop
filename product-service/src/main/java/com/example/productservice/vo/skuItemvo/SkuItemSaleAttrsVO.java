package com.example.productservice.vo.skuItemvo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SkuItemSaleAttrsVO {
    private Long attrId;
    private String attrName;
    private List<AttrValueWithSkuIdVO> attrValues;
}
