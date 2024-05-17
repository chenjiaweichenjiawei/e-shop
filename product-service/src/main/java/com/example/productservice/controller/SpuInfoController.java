package com.example.productservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.util.Result;
import com.example.productservice.po.SpuInfo;
import com.example.productservice.service.SpuInfoService;
import com.example.productservice.vo.spusavevo.SpuSaveVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * spu信息 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/spuInfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    @PostMapping("/save")
    public Result<Void> save(@RequestBody SpuSaveVO vo) {
        spuInfoService.saveSpuInfo(vo);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<Page<SpuInfo>> list(@RequestParam Map<String, Object> params) {
        Page<SpuInfo> page = spuInfoService.queryPageByParams(params);
        return Result.success(page);
    }

    @PostMapping("/{spuId}/up")
    public Result<Void> spuUp(@PathVariable("spuId") Long spuId) throws IOException {
        spuInfoService.up(spuId);
        return Result.success();
    }

    @GetMapping("/skuId/{id}")
    public Result<SpuInfo> getSpuInfoBySkuId(@PathVariable("id") Long skuId) {
        SpuInfo entity = spuInfoService.getSpuInfoBySkuId(skuId);
        return Result.success(entity);
    }
}
