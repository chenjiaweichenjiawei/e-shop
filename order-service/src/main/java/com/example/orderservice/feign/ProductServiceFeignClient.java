package com.example.orderservice.feign;

import com.example.common.util.Result;
import com.example.orderservice.vo.OrderItemVO;
import com.example.orderservice.vo.SpuInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author CJW
 * @since 2024/5/15
 */
@FeignClient("product-service")
public interface ProductServiceFeignClient {
    @GetMapping("/cart/currentUserCartItems")
    Result<List<OrderItemVO>> getCurrentUserCartItems(@RequestParam("userId") Long userId);

    @GetMapping("/spuInfo/skuId/{id}")
    Result<SpuInfoVO> getSpuInfoBySkuId(@PathVariable("id") Long skuId);
}
