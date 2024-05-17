package com.example.couponservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.dto.SkuReductionDTO;
import com.example.couponservice.po.SkuFullReduction;

/**
 * <p>
 * 商品满减信息 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface SkuFullReductionService extends IService<SkuFullReduction> {
    /**
     * 保存满减优惠信息
     *
     * @param skuReductionDTO
     */
    void saveSkuReduction(SkuReductionDTO skuReductionDTO);
}
