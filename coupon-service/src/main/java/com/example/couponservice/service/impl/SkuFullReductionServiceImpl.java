package com.example.couponservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.dto.MemberPrice;
import com.example.common.dto.SkuReductionDTO;
import com.example.couponservice.mapper.SkuFullReductionMapper;
import com.example.couponservice.po.SkuFullReduction;
import com.example.couponservice.po.SkuLadder;
import com.example.couponservice.service.MemberPriceService;
import com.example.couponservice.service.SkuFullReductionService;
import com.example.couponservice.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品满减信息 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionMapper, SkuFullReduction> implements SkuFullReductionService {
    @Autowired
    private SkuLadderService skuLadderService;
    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public void saveSkuReduction(SkuReductionDTO skuReductionDTO) {
        //1.保存满减打折,会员价
        SkuLadder skuLadder = new SkuLadder();
        skuLadder.setSkuId(skuReductionDTO.getSkuId());
        skuLadder.setFullCount(skuReductionDTO.getFullCount());
        skuLadder.setDiscount(skuReductionDTO.getDiscount());
        skuLadder.setAddOther(skuReductionDTO.getCountStatus());
        if (skuReductionDTO.getFullCount() > 0) {
            skuLadderService.save(skuLadder);
        }
        //2.sms_sku_full_reduction
        SkuFullReduction skuFullReduction = new SkuFullReduction();
        BeanUtils.copyProperties(skuReductionDTO, skuFullReduction);
        this.save(skuFullReduction);

        //3.sms_member_price
        List<MemberPrice> memberPriceList = skuReductionDTO.getMemberPrice();
        List<com.example.couponservice.po.MemberPrice> collect = memberPriceList.stream().map(item -> {
            com.example.couponservice.po.MemberPrice memberPrice = new com.example.couponservice.po.MemberPrice();
            memberPrice.setSkuId(skuReductionDTO.getSkuId());
            memberPrice.setMemberLevelId(item.getId());
            memberPrice.setMemberLevelName(item.getName());
            memberPrice.setMemberPrice(item.getPrice());
            memberPrice.setAddOther(1);
            return memberPrice;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(collect);
    }
}
