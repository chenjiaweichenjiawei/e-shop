package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.SpuInfo;
import com.example.productservice.vo.spusavevo.SpuSaveVO;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * spu信息 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface SpuInfoService extends IService<SpuInfo> {
    /**
     * 保存spu
     *
     * @param vo
     */
    void saveSpuInfo(SpuSaveVO vo);

    /**
     * spu信息检索
     *
     * @param params
     * @return
     */
    Page<SpuInfo> queryPageByParams(Map<String, Object> params);

    /**
     * 商品上架
     *
     * @param spuId
     */
    void up(Long spuId) throws IOException;

    /**
     * 根据skuId获取spu信息
     *
     * @param skuId
     * @return
     */
    SpuInfo getSpuInfoBySkuId(Long skuId);
}
