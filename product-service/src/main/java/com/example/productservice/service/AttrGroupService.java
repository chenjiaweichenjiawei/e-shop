package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.AttrGroup;
import com.example.productservice.vo.AttrGroupVO;
import com.example.productservice.vo.AttrGroupWithAttrsVO;
import com.example.productservice.vo.skuItemvo.SpuItemAttrGroupVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 属性分组 服务类
 * </p>
 *
 * @author CJW
 * @since 2023-11-21
 */
public interface AttrGroupService extends IService<AttrGroup> {
    /**
     * 获取分类属性分组
     *
     * @param catId    分类id
     * @param paramMap 其他统一参数
     * @return
     */
    Page<AttrGroup> getByCatId(Long catId, Map<String, String> paramMap);

    /**
     * 获取属性分组的详细信息
     *
     * @param attrGroupId 属性分组的id
     * @return
     */
    AttrGroupVO getInfoById(Long attrGroupId);

    /**
     * 获取某分类下所有分组和属性
     *
     * @param catelogId
     * @return
     */
    List<AttrGroupWithAttrsVO> getAttrGroupWithAttrsByCatelogId(Long catelogId);

    /**
     * 获取spu规格参数
     *
     * @param spuId
     * @param catalogId
     * @return
     */
    List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId);
}
