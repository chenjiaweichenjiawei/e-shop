package com.example.productservice.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.productservice.po.SkuInfo;
import com.example.productservice.service.CartService;
import com.example.productservice.service.SkuInfoService;
import com.example.productservice.service.SkuSaleAttrValueService;
import com.example.productservice.vo.cart.CartItem;
import com.example.productservice.vo.cart.SkuInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CJW
 * @since 2024/5/15
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    private static final String CART_PREFIX = "cart:";

    @Override
    public CartItem addToCart(Long skuId, Integer num, Long userId) {
        BoundHashOperations<String, Object, Object> cartHashOperations = stringRedisTemplate.boundHashOps(CART_PREFIX + userId.toString());
        String res = (String) cartHashOperations.get(skuId.toString());
        if (StringUtils.isEmpty(res)) {
            //购物车中无此商品,新商品添加到购物车
            CartItem cartItem = new CartItem();
            //远程查询当前要添加的商品信息
            SkuInfo skuInfo = skuInfoService.getById(skuId);
            SkuInfoVO skuInfoVO = new SkuInfoVO();
            BeanUtils.copyProperties(skuInfo, skuInfoVO);
            cartItem.setCheck(true);
            cartItem.setCount(num);
            cartItem.setImage(skuInfoVO.getSkuDefaultImg());
            cartItem.setTitle(skuInfoVO.getSkuTitle());
            cartItem.setSkuId(skuId);
            cartItem.setPrice(skuInfoVO.getPrice());
            //远程查询sku的组合信息
            List<String> values = skuSaleAttrValueService.getSkuSaleAttrValuesAsStringList(skuId);
            cartItem.setSkuAttr(values);
            //把cartItem对象转换为json格式
            String string = JSON.toJSONString(cartItem);
            cartHashOperations.put(skuId.toString(), string);
            return cartItem;
        } else {
            //购物车有此商品,修改数量
            //将json逆转为CartItem
            CartItem cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);
            //再将CartItem换回json存入redis
            cartHashOperations.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    @Override
    public void checkItem(Long skuId, Integer check, Long userId) {
        BoundHashOperations<String, Object, Object> cartHashOperations = stringRedisTemplate.boundHashOps(CART_PREFIX + userId.toString());
        String str = (String) cartHashOperations.get(skuId.toString());
        CartItem cartItem = com.alibaba.fastjson.JSON.parseObject(str, CartItem.class);
        cartItem.setCheck(check == 1 ? true : false);
        String string = com.alibaba.fastjson.JSON.toJSONString(cartItem);
        cartHashOperations.put(skuId.toString(), string);
    }

    @Override
    public void countItem(Long skuId, Integer num, Long userId) {
        BoundHashOperations<String, Object, Object> cartHashOperations = stringRedisTemplate.boundHashOps(CART_PREFIX + userId.toString());
        String str = (String) cartHashOperations.get(skuId.toString());
        CartItem cartItem = com.alibaba.fastjson.JSON.parseObject(str, CartItem.class);
        cartItem.setCount(num);
        String s = com.alibaba.fastjson.JSON.toJSONString(cartItem);
        cartHashOperations.put(skuId.toString(), s);
    }

    @Override
    public void deleteItem(Long skuId, Long userId) {
        BoundHashOperations<String, Object, Object> cartHashOperations = stringRedisTemplate.boundHashOps(CART_PREFIX + userId.toString());
        cartHashOperations.delete(skuId.toString());
    }

    @Override
    public List<CartItem> getUserCartItems(Long userId) {
        String cartKey = CART_PREFIX + userId;
        List<CartItem> cartItems = getCartItems(cartKey);
        //获取所有被选中的购物项
        return cartItems.stream()
                .filter(CartItem::getCheck)
                .map(item -> {
                    String price = skuInfoService.getById(item.getSkuId()).getPrice().toString();
                    //更新为最新价格
                    item.setPrice(new BigDecimal(price));
                    return item;
                }).collect(Collectors.toList());
    }

    private List<CartItem> getCartItems(String cartKey) {
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if (values != null && values.size() > 0) {
            return values.stream().map((obj) -> {
                String str = (String) obj;
                return com.alibaba.fastjson.JSON.parseObject(str, CartItem.class);
            }).collect(Collectors.toList());
        }
        return null;
    }
}

