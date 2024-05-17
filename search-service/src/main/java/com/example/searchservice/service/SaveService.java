package com.example.searchservice.service;

import com.example.common.dto.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author CJW
 * @since 2024/5/13
 */
public interface SaveService {
    /**
     * 商品上架保存es模型数据进去es
     *
     * @param skuEsModels
     */
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
