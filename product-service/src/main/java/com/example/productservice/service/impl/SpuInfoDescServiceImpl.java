package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.SpuInfoDescMapper;
import com.example.productservice.po.SpuInfoDesc;
import com.example.productservice.service.SpuInfoDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * spu信息介绍 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class SpuInfoDescServiceImpl extends ServiceImpl<SpuInfoDescMapper, SpuInfoDesc> implements SpuInfoDescService {
    @Autowired
    private SpuInfoDescMapper spuInfoDescMapper;

    @Override
    public SpuInfoDesc getBySpuId(Long spuId) {
        LambdaQueryWrapper<SpuInfoDesc> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SpuInfoDesc::getSpuId, spuId);
        return spuInfoDescMapper.selectOne(queryWrapper);
    }
}
