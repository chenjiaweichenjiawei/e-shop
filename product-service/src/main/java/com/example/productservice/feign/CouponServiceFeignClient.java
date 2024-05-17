package com.example.productservice.feign;

import com.example.common.dto.SkuReductionDTO;
import com.example.common.dto.SpuBoundDTO;
import com.example.common.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author CJW
 * @since 2024/5/10
 */
@FeignClient("coupon-service")
public interface CouponServiceFeignClient {

    /**
     * 此处传的数据对象不一定要求和远程服务的接收对象一致，只要其中的属性名一致就行
     * 只要json数据模型兼容，spring cloud会自行处理
     *
     * @param spuBoundDTO
     * @return
     */
    @PostMapping("/spuBounds/save")
    Result<Void> saveBounds(@RequestBody SpuBoundDTO spuBoundDTO);

    /**
     * 保存满减信息
     *
     * @param skuReductionDTO
     * @return
     */
    @PostMapping("/skuFullReduction/saveInfo")
    Result<Void> saveFullReduction(@RequestBody SkuReductionDTO skuReductionDTO);
}
