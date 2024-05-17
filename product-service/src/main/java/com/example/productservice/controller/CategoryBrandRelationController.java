package com.example.productservice.controller;


import com.example.common.util.Result;
import com.example.productservice.po.Brand;
import com.example.productservice.po.CategoryBrandRelation;
import com.example.productservice.service.CategoryBrandRelationService;
import com.example.productservice.vo.BrandVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 品牌分类关联 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-02-27
 */
@RestController
@RequestMapping("/categoryBrandRelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @GetMapping("/save")
    public Result<Void> save(@RequestBody CategoryBrandRelation categoryBrandRelation) {
        categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return Result.success();
    }

    /**
     * 获取分类关联的品牌
     *
     * @param catId
     * @return
     */
    @GetMapping("/brands/list")
    public Result<List<BrandVO>> relationBrandList(@RequestParam(value = "catId", required = true) Long catId) {
        List<Brand> brands = categoryBrandRelationService.getBrandsByCatId(catId);
        List<BrandVO> collect = brands.stream()
                .map(item -> {
                    BrandVO brandVo = new BrandVO();
                    brandVo.setBrandId(item.getBrandId());
                    brandVo.setBrandName(item.getName());
                    return brandVo;
                }).collect(Collectors.toList());
        return Result.success(collect);
    }

}
