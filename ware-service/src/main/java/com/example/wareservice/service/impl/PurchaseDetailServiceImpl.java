package com.example.wareservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.util.PageUtils;
import com.example.wareservice.mapper.PurchaseDetailMapper;
import com.example.wareservice.po.PurchaseDetail;
import com.example.wareservice.service.PurchaseDetailService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailMapper, PurchaseDetail> implements PurchaseDetailService {

    @Override
    public Page<PurchaseDetail> queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseDetail> queryWrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(w -> {
                w.eq(PurchaseDetail::getPurchaseId, key).or().eq(PurchaseDetail::getSkuId, key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq(PurchaseDetail::getStatus, status);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            queryWrapper.eq(PurchaseDetail::getWareId, wareId);
        }

        return this.page(PageUtils.getPage(params), queryWrapper);
    }
}
