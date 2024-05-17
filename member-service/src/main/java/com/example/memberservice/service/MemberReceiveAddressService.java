package com.example.memberservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.memberservice.po.MemberReceiveAddress;

import java.util.List;

/**
 * <p>
 * 会员收货地址 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddress> {
    /**
     * 获取用户的收获地址信息
     *
     * @param memberId
     * @return
     */
    List<MemberReceiveAddress> getAddress(Long memberId);
}
