package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.Brand;

/**
 * <p>
 * 品牌 服务类
 * </p>
 *
 * @author CJW
 * @since 2023-11-14
 */
public interface BrandService extends IService<Brand> {
    /**
     * 更新品牌数据
     *
     * @param brand
     */
    void updateDetail(Brand brand);
}
