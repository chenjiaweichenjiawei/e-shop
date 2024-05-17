package com.example.wareservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.WareConstant;
import com.example.wareservice.mapper.PurchaseMapper;
import com.example.wareservice.po.Purchase;
import com.example.wareservice.po.PurchaseDetail;
import com.example.wareservice.service.PurchaseDetailService;
import com.example.wareservice.service.PurchaseService;
import com.example.wareservice.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 采购信息 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class PurchaseServiceImpl extends ServiceImpl<PurchaseMapper, Purchase> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        List<Long> items = mergeVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<Long> collect = items.stream().filter(i -> {
            PurchaseDetail purchaseDetail = purchaseDetailService.getById(i);
            return purchaseDetail.getStatus() == WareConstant.PurchaseDetailStatusEnum.CREATED.getCode()
                    || purchaseDetail.getStatus() == WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode();
        }).collect(Collectors.toList());
        if (collect.size() > 0) {
            //新建采购单的情况
            if (purchaseId == null) {
                Purchase purchase = new Purchase();
                purchase.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
                this.save(purchase);
                purchaseId = purchase.getId();
            }
            List<PurchaseDetail> collect1 = collect.stream().map(i -> {
                PurchaseDetail purchaseDetail = purchaseDetailService.getById(i);
                purchaseDetail.setPurchaseId(finalPurchaseId);
                purchaseDetail.setId(i);
                purchaseDetail.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                return purchaseDetail;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(collect1);
            //优化时间更新
            Purchase purchase = new Purchase();
            purchase.setId(purchaseId);
            //领取采购单最终是更新操作
            return this.updateById(purchase);
        } else {
            return false;
        }
    }
}
