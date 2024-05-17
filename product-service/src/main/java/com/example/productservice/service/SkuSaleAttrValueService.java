package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.SkuSaleAttrValue;
import com.example.productservice.vo.skuItemvo.SkuItemSaleAttrsVO;

import java.util.List;

/**
 * <p>
 * sku销售属性&值 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValue> {
    /**
     * 获取销售属性组合
     *
     * @param spuId
     * @return
     */
    List<SkuItemSaleAttrsVO> getSaleAttrsBySpuId(Long spuId);

    List<String> getSkuSaleAttrValuesAsStringList(Long skuId);
}
