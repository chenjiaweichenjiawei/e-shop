package com.example.memberservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.memberservice.mapper.MemberReceiveAddressMapper;
import com.example.memberservice.po.MemberReceiveAddress;
import com.example.memberservice.service.MemberReceiveAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 会员收货地址 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Service
public class MemberReceiveAddressServiceImpl extends ServiceImpl<MemberReceiveAddressMapper, MemberReceiveAddress> implements MemberReceiveAddressService {

    @Override
    public List<MemberReceiveAddress> getAddress(Long memberId) {
        return this.list(
                new LambdaQueryWrapper<MemberReceiveAddress>()
                        .eq(MemberReceiveAddress::getMemberId, memberId));
    }
}
