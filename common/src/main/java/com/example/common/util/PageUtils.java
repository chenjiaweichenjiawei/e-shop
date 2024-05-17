package com.example.common.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.constant.PageConstant;

import java.util.Map;

/**
 * @author CJW
 * @since 2024/5/10
 */
public class PageUtils {
    public static <T> Page<T> getPage(Map<String, Object> params) {
        return getPage(params, null, false);
    }

    public static <T> Page<T> getPage(Map<String, Object> params, String defaultOrderField, boolean isAsc) {
        //分页参数
        long currentPage = 1;
        long pageSize = 10;

        if (params.get(PageConstant.CURRENT_PAGE) != null) {
            currentPage = Long.parseLong(params.get(PageConstant.CURRENT_PAGE).toString());
        }
        if (params.get(PageConstant.PAGE_SIZE) != null) {
            pageSize = Long.parseLong((String) params.get(PageConstant.PAGE_SIZE));
        }

        //分页对象
        Page<T> page = new Page<>(currentPage, pageSize);


        //排序字段
        String orderField = (String) params.get(PageConstant.ORDER_FIELD);
        String order = (String) params.get(PageConstant.ORDER);
        if (StringUtils.isNotEmpty(orderField) && StringUtils.isNotEmpty(order)) {
            if (PageConstant.ASC.equalsIgnoreCase(order)) {
                page.addOrder(OrderItem.asc(orderField));
            } else {
                page.addOrder(OrderItem.desc(orderField));
            }
        }

        //有后端自定义排序字段，则进行排序排序
        if (!StringUtils.isBlank(defaultOrderField)) {
            if (isAsc) {
                page.addOrder(OrderItem.asc(defaultOrderField));
            } else {
                page.addOrder(OrderItem.desc(defaultOrderField));
            }
        }
        return page;
    }
}
