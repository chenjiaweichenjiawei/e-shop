package com.example.orderservice.feign;

import com.example.common.util.Result;
import com.example.orderservice.vo.FareVO;
import com.example.orderservice.vo.SkuStockVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author CJW
 * @since 2024/5/15
 */
@FeignClient("ware-service")
public interface WareServiceFeignClient {
    @PostMapping("/wareSku/hasStock")
    Result<List<SkuStockVO>> getSkuHasStock(@RequestBody List<Long> skuIds);

    @GetMapping("/wareInfo/fare")
    Result<FareVO> getFare(@RequestParam("addrId") Long addrId);
}
