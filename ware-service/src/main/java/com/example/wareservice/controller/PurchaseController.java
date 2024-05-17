package com.example.wareservice.controller;


import com.example.common.util.Result;
import com.example.wareservice.service.PurchaseService;
import com.example.wareservice.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 采购信息 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    @PostMapping("/merge")
    public Result<Void> merge(@RequestBody MergeVo mergeVo) {
        boolean flag = purchaseService.mergePurchase(mergeVo);
        if (flag) {
            return Result.success();
        } else {
            return Result.error("请选择新建或已分配的采购需求");
        }
    }
}
