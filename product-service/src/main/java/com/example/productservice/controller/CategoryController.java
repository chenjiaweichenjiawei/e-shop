package com.example.productservice.controller;


import com.example.common.util.Result;
import com.example.productservice.po.Category;
import com.example.productservice.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 商品三级分类 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2023-11-09
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> getCategoryListTree() {
        List<Category> categoryList = categoryService.getCategoryTree();
        return Result.success(categoryList);
    }

    @DeleteMapping
    public Result<Void> delete(@RequestBody Long[] catIds) {
        categoryService.deleteByIds(catIds);
        return Result.success();
    }

    @DeleteMapping("/{catId}")
    public Result<Void> deleteOne(@PathVariable("catId") Long catId) {
        categoryService.removeById(catId);
        return Result.success();
    }


    @PostMapping
    public Result<Void> insert(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody Category category) {
        categoryService.updateDetail(category);
        return Result.success();
    }

    @GetMapping("/{catId}")
    public Result<Category> getById(@PathVariable("catId") Long catId) {
        Category category = categoryService.getById(catId);
        return Result.success(category);
    }


    @GetMapping("/path/{catId}")
    public Result<List<Long>> getPathByCatId(@PathVariable Long catId) {
        List<Long> path = categoryService.getPathByCatId(catId);
        return Result.success(path);
    }
}
