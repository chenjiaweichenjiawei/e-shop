package com.example.productservice.controller;

import com.example.common.util.Result;
import com.example.productservice.service.CartService;
import com.example.productservice.vo.cart.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author CJW
 * @since 2024/5/15
 */
@RequestMapping("/cart")
@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/addToCart")
    public Result<Void> addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                                  @RequestParam("userId") Long userId) {
        cartService.addToCart(skuId, num, userId);
        return Result.success();
    }

    @PutMapping("/checkItem")
    public Result<Void> checkItem(@RequestParam("skuId") Long skuId, @RequestParam("check") Integer check,
                                  @RequestParam("userId") Long userId) {
        cartService.checkItem(skuId, check, userId);
        return Result.success();
    }

    @PutMapping("/countItem")
    public Result<Void> countItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num,
                                  @RequestParam("userId") Long userId) {
        cartService.countItem(skuId, num, userId);
        return Result.success();
    }

    @DeleteMapping("/deleteItem")
    public Result<Void> deleteItem(@RequestParam("skuId") Long skuId, @RequestParam("userId") Long userId) {
        cartService.deleteItem(skuId, userId);
        return Result.success();
    }

    @GetMapping("/currentUserCartItems")
    public Result<List<CartItem>> getCurrentUserCartItems(@RequestParam("userId") Long userId) {
        return Result.success(cartService.getUserCartItems(userId));
    }
}
