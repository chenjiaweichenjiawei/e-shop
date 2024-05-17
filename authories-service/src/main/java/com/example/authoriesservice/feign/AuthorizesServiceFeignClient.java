package com.example.authoriesservice.feign;

import com.alibaba.nacos.api.model.v2.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author CJW
 * @since 2023/11/30
 */
@FeignClient("third-service")
public interface AuthorizesServiceFeignClient {
    @GetMapping("/sms/sendCode")
    Result<Void> sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);
}
