package com.example.productservice.feign;

import com.example.common.dto.es.SkuEsModel;
import com.example.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.List;

/**
 * @author CJW
 * @since 2024/5/13
 */
@FeignClient("search-service")
public interface SearchServiceFeignClient {
    @PostMapping("/save/product/up")
    Result<Void> productStatuesUp(@RequestBody List<SkuEsModel> skuEsModels) throws IOException;
}
