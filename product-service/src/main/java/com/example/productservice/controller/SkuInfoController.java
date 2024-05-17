package com.example.productservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.util.Result;
import com.example.productservice.po.SkuInfo;
import com.example.productservice.service.SkuInfoService;
import com.example.productservice.vo.skuItemvo.SkuItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * sku信息 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/skuInfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    @GetMapping("/list")
    public Result<Page<SkuInfo>> list(@RequestParam Map<String, Object> params) {
        Page<SkuInfo> page = skuInfoService.queryPageByParams(params);
        return Result.success(page);
    }

    @GetMapping("/{skuId}")
    public Result<SkuItemVO> getSkuItemVO(@PathVariable Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVO skuItemVO = skuInfoService.getSkuItem(skuId);
        return Result.success(skuItemVO);
    }

}
