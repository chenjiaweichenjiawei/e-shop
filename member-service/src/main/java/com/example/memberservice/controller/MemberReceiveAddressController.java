package com.example.memberservice.controller;


import com.example.common.util.Result;
import com.example.memberservice.po.MemberReceiveAddress;
import com.example.memberservice.service.MemberReceiveAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 会员收货地址 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@RestController
@RequestMapping("/memberReceiveAddress")
public class MemberReceiveAddressController {
    @Autowired
    private MemberReceiveAddressService memberReceiveAddressService;

    @GetMapping("/{memberId}/addresses")
    public Result<List<MemberReceiveAddress>> getAddress(@PathVariable("memberId") Long memberId) {
        return Result.success(memberReceiveAddressService.getAddress(memberId));
    }

    @GetMapping("/info/{id}")
    public Result<MemberReceiveAddress> addrInfo(@PathVariable("id") Long id) {
        MemberReceiveAddress memberReceiveAddress = memberReceiveAddressService.getById(id);
        return Result.success(memberReceiveAddress);
    }

}
