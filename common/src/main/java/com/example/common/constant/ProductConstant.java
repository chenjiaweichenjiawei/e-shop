package com.example.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author CJW
 */
public class ProductConstant {
    @Getter
    @AllArgsConstructor
    public enum AttrEnum {
        /**
         * 商品属性常量
         */
        ATTR_TYPE_BASE(1, "基本属性"),
        ATTR_TYPE_SALE(0, "销售属性");

        private final int code;

        private final String msg;
    }

    @Getter
    @AllArgsConstructor
    public enum StatusEnum {
        /**
         * 商品状态常量
         */
        SPU_NEW(0, "新建"),
        SPU_UP(1, "上架"),
        SPU_DOWN(2, "下架");
        private final int code;

        private final String msg;

    }
}
