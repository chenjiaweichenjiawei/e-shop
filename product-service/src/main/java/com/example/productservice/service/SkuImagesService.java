package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.SkuImages;

import java.util.List;

/**
 * <p>
 * sku图片 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface SkuImagesService extends IService<SkuImages> {
    /**
     * 获取sku图片信息
     *
     * @param skuId
     * @return
     */
    List<SkuImages> getImagesBySkuId(Long skuId);
}
