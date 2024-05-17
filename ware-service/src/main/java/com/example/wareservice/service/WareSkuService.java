package com.example.wareservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.dto.SkuHasStockDTO;
import com.example.wareservice.po.WareSku;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品库存 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface WareSkuService extends IService<WareSku> {
    /**
     * 查询sku库存信息
     *
     * @param params
     * @return
     */
    Page<WareSku> queryPage(Map<String, Object> params);

    /**
     * 查询sku的库存信息
     *
     * @param skuIds
     * @return
     */
    List<SkuHasStockDTO> getSkuHasStock(List<Long> skuIds);
}
