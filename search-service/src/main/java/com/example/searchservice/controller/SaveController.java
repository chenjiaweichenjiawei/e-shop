package com.example.searchservice.controller;

import com.example.common.dto.es.SkuEsModel;
import com.example.common.util.Result;
import com.example.searchservice.service.SaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author CJW
 * @since 2024/5/13
 */
@Slf4j
@RequestMapping("/save")
@RestController
public class SaveController {
    @Autowired
    private SaveService saveService;

    @PostMapping("/product/up")
    public Result<Void> productStatuesUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean fail;
        try {
            fail = saveService.productStatusUp(skuEsModels);
        } catch (Exception e) {
            log.error("es商品上架胡数据出错{}", e);
            return Result.error("es商品上架数据出错");
        }
        if (!fail) {
            return Result.success();
        }
        return Result.error("es商品上架数据出错");
    }
}
