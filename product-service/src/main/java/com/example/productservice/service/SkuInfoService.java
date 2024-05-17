package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.SkuInfo;
import com.example.productservice.vo.skuItemvo.SkuItemVO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * sku信息 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface SkuInfoService extends IService<SkuInfo> {
    /**
     * sku信息检索
     *
     * @param params
     * @return
     */
    Page<SkuInfo> queryPageByParams(Map<String, Object> params);

    /**
     * 获取spuId所有的sku信息
     *
     * @param spuId
     * @return
     */
    List<SkuInfo> getSkusBySpuId(Long spuId);

    /**
     * 获取指定商品展示页的详细信息
     *
     * @param skuId
     * @return
     */
    SkuItemVO getSkuItem(Long skuId) throws ExecutionException, InterruptedException;
}
