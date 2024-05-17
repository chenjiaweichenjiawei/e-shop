package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.SpuInfoDesc;

/**
 * <p>
 * spu信息介绍 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface SpuInfoDescService extends IService<SpuInfoDesc> {

    SpuInfoDesc getBySpuId(Long spuId);
}
