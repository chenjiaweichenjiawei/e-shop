package com.example.couponservice.controller;


import com.example.common.dto.SkuReductionDTO;
import com.example.common.util.Result;
import com.example.couponservice.service.SkuFullReductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品满减信息 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/skuFullReduction")
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;

    @PostMapping("/saveInfo")
    public Result<Void> saveInfo(@RequestBody SkuReductionDTO skuReductionDTO) {
        skuFullReductionService.saveSkuReduction(skuReductionDTO);
        return Result.success();
    }
}
