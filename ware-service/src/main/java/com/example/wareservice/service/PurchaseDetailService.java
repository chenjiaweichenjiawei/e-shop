package com.example.wareservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wareservice.po.PurchaseDetail;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface PurchaseDetailService extends IService<PurchaseDetail> {
    /**
     * 查询采购需求
     *
     * @param params
     * @return
     */
    Page<PurchaseDetail> queryPage(Map<String, Object> params);
}
