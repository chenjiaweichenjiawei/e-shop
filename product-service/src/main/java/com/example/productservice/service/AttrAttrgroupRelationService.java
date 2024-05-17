package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.AttrAttrgroupRelation;
import com.example.productservice.vo.AttrGroupRelationVO;

import java.util.List;

/**
 * <p>
 * 属性&属性分组关联 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-03-03
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelation> {
    /**
     * 保存属性与分组的关联关系
     *
     * @param vos
     */
    void saveBatch(List<AttrGroupRelationVO> vos);
}
