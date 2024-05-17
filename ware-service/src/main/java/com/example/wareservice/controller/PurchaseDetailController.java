package com.example.wareservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.util.Result;
import com.example.wareservice.po.PurchaseDetail;
import com.example.wareservice.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/purchaseDetail")
public class PurchaseDetailController {
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @GetMapping("/list")
    public Result<Page<PurchaseDetail>> list(@RequestParam Map<String, Object> params) {
        Page<PurchaseDetail> page = purchaseDetailService.queryPage(params);
        return Result.success(page);
    }
}
