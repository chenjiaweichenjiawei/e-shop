package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.ProductAttrValue;

import java.util.List;

/**
 * <p>
 * spu属性值 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface ProductAttrValueService extends IService<ProductAttrValue> {
    /**
     * 获取当前spuId中所有可以用来检索的属性
     *
     * @param spuId
     * @return
     */
    List<ProductAttrValue> baseAttrListForSpu(Long spuId);
}
