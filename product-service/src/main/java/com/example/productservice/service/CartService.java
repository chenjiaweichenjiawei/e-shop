package com.example.productservice.service;

import com.example.productservice.vo.cart.CartItem;

import java.util.List;

/**
 * @author CJW
 * @since 2024/5/15
 */
public interface CartService {
    /**
     * 添加商品到购物车
     *
     * @param skuId
     * @param num
     * @param userId
     * @return
     */
    CartItem addToCart(Long skuId, Integer num, Long userId);

    /**
     * 选中商品
     *
     * @param skuId
     * @param check
     * @param userId
     */
    void checkItem(Long skuId, Integer check, Long userId);

    /**
     * 改变商品数量
     *
     * @param skuId
     * @param num
     * @param userId
     */
    void countItem(Long skuId, Integer num, Long userId);

    /**
     * 删除购物车项
     *
     * @param skuId
     * @param userId
     */
    void deleteItem(Long skuId, Long userId);

    /**
     * 获取用户的购物车条目
     *
     * @param userId
     * @return
     */
    List<CartItem> getUserCartItems(Long userId);
}
