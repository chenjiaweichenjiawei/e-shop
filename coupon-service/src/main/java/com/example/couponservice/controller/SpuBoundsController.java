package com.example.couponservice.controller;


import com.example.common.util.Result;
import com.example.couponservice.po.SpuBounds;
import com.example.couponservice.service.SpuBoundsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品spu积分设置 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/spuBounds")
public class SpuBoundsController {
    @Autowired
    private SpuBoundsService spuBoundsService;

    @PostMapping("/save")
    public Result<Void> save(@RequestBody SpuBounds spuBounds) {
        spuBoundsService.save(spuBounds);
        return Result.success();
    }

}
