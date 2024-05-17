package com.example.productservice.feign;

import com.example.common.dto.SkuHasStockDTO;
import com.example.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author CJW
 * @since 2024/5/13
 */
@FeignClient("ware-service")
public interface WareServiceFeignClient {
    @PostMapping("/wareSku/hasStock")
    Result<List<SkuHasStockDTO>> getSkuHasStock(@RequestBody List<Long> skuIds);
}
