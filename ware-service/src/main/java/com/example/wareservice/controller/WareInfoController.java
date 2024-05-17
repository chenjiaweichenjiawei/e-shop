package com.example.wareservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.util.Result;
import com.example.wareservice.po.WareInfo;
import com.example.wareservice.service.WareInfoService;
import com.example.wareservice.vo.FareVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 仓库信息 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@RestController
@RequestMapping("/wareInfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;

    @GetMapping("/list")
    public Result<Page<WareInfo>> list(@RequestParam Map<String, Object> params) {
        Page<WareInfo> page = wareInfoService.queryPage(params);
        return Result.success(page);
    }

    @GetMapping("/fare")
    public Result<FareVO> getFare(@RequestParam("addrId") Long addrId) {
        FareVO fare = wareInfoService.getFare(addrId);
        return Result.success(fare);
    }
}
