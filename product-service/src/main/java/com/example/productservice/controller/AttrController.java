package com.example.productservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.util.Result;
import com.example.productservice.service.AttrService;
import com.example.productservice.vo.AttrRespVO;
import com.example.productservice.vo.AttrVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 商品属性 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-03-03
 */
@RestController
@RequestMapping("/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @PostMapping("/save")
    public Result<Void> save(@RequestBody AttrVO attrVO) {
        attrService.saveDetail(attrVO);
        return Result.success();
    }

    @GetMapping("/list/{categoryId}")
    public Result<Page<AttrRespVO>> getAttrPageById(@RequestParam Map<String, String> params,
                                                    @PathVariable("categoryId") Long categoryId) {
        Page<AttrRespVO> attrRespVoPage = attrService.queryAttrPage(params, categoryId);
        return Result.success(attrRespVoPage);
    }

    @GetMapping("/info/{attrId}")
    public Result<AttrRespVO> getInfo(@PathVariable Long attrId) {
        AttrRespVO attrRespVo = attrService.getInfoById(attrId);
        return Result.success(attrRespVo);
    }

    @PutMapping("/update")
    public Result<Void> update(@RequestBody AttrVO attrVO) {
        attrService.updateAttr(attrVO);
        return Result.success();
    }
}
