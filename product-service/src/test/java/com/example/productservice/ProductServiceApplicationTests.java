package com.example.productservice;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.example.productservice.mapper.AttrGroupMapper;
import com.example.productservice.po.Category;
import com.example.productservice.util.RedisUtils;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProductServiceApplicationTests {
    @Autowired
    private AttrGroupMapper attrGroupMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedissonClient redissonClient;

    @Test
    void contextLoads() {
        String s = redisUtils.get("product-service:category:getCategoryTree:");
        List<Category> list = JSON.parseObject(s, new TypeReference<List<Category>>() {
        });
        list.forEach(System.out::println);
    }
}
