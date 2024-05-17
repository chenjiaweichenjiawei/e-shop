package com.example.wareservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wareservice.po.Purchase;
import com.example.wareservice.vo.MergeVo;

/**
 * <p>
 * 采购信息 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface PurchaseService extends IService<Purchase> {
    /**
     * 合并采购单
     *
     * @param mergeVo
     * @return
     */
    boolean mergePurchase(MergeVo mergeVo);
}
