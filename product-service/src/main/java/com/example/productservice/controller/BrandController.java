package com.example.productservice.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.common.util.Result;
import com.example.productservice.po.Brand;
import com.example.productservice.service.BrandService;
import com.example.productservice.validation.AddBrand;
import com.example.productservice.validation.UpdateBrand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 品牌 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2023-11-14
 */
@RestController
@RequestMapping("/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    @GetMapping
    public Result<List<Brand>> get() {
        //获取全部的品牌信息
        List<Brand> brands = brandService.list();
        return Result.success(brands);
    }

    @PutMapping
    public Result<Void> update(@RequestBody List<Brand> brands) {
        //批量修改品牌的显示状态
        for (Brand brand : brands) {
            UpdateWrapper<Brand> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("brand_id", brand.getBrandId());
            updateWrapper.set("show_status", brand.getShowStatus());
            brandService.update(updateWrapper);
        }
        return Result.success();
    }

    @PostMapping
    public Result<Void> insert(@Validated(AddBrand.class) @RequestBody Brand brand) {
        //新增品牌
        brandService.save(brand);
        return Result.success();
    }

    @PutMapping("/update")
    public Result<Void> updateOne(@Validated(UpdateBrand.class) @RequestBody Brand brand) {
        //更新品牌
        brandService.updateDetail(brand);
        return Result.success();
    }
}
