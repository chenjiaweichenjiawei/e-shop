package com.example.authoriesservice.controller;


import com.example.authoriesservice.feign.AuthorizesServiceFeignClient;
import com.example.authoriesservice.vo.UserRegisterVO;
import com.example.common.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @author CJW
 * @since 2023/11/30
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AuthorizesServiceFeignClient authorizesServiceFeignClient;

    @GetMapping("/sendCode")
    public Result<String> sendCode(@RequestParam String phone) {
        //验证码储存并防刷策略
        String redisData = stringRedisTemplate.opsForValue().get(phone);
        if (redisData == null) {
            String code = "6666";
            stringRedisTemplate.opsForValue().set(phone, code + "-" + System.currentTimeMillis(), 5, TimeUnit.MINUTES);
            //authoriesServiceFeignClient.sendCode(phone,code);
            return Result.success();
        }
        long millis = Long.parseLong(redisData.split("-")[1]);
        if (System.currentTimeMillis() - millis <= 60000) {
            return Result.error("请稍后重试");
        }
        String code = "6666";
        stringRedisTemplate.opsForValue().set(phone, code + "-" + System.currentTimeMillis(), 5, TimeUnit.MINUTES);
        //authoriesServiceFeignClient.sendCode(phone,code);
        return Result.success();
    }

    @PostMapping
    public Result<String> register(@RequestBody UserRegisterVO userRegisterVO) {
        String cacheCode = stringRedisTemplate.opsForValue().get(userRegisterVO.getPhone());
        if (cacheCode == null) {
            return Result.error("验证码失效");
        }
        String code = cacheCode.split("-")[0];
        if (!code.equals(userRegisterVO.getCode())) {
            return Result.error("验证码错误");
        }
        return null;
    }
}
