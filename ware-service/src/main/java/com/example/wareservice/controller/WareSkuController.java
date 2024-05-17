package com.example.wareservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.dto.SkuHasStockDTO;
import com.example.common.util.Result;
import com.example.wareservice.po.WareSku;
import com.example.wareservice.service.WareSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品库存 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/wareSku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    @GetMapping("/list")
    public Result<Page<WareSku>> list(@RequestParam Map<String, Object> params) {
        Page<WareSku> page = wareSkuService.queryPage(params);
        return Result.success(page);
    }

    @PostMapping("/hasStock")
    public Result<List<SkuHasStockDTO>> getSkuHasStock(@RequestBody List<Long> skuIds) {
        List<SkuHasStockDTO> list = wareSkuService.getSkuHasStock(skuIds);
        return Result.success(list);
    }
}
