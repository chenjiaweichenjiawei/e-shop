package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.Attr;
import com.example.productservice.vo.AttrGroupRelationVO;
import com.example.productservice.vo.AttrRespVO;
import com.example.productservice.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品属性 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-03-03
 */
public interface AttrService extends IService<Attr> {
    /**
     * 新增属性
     *
     * @param attrVO
     */
    void saveDetail(AttrVO attrVO);

    /**
     * 获取分类的属性分页数据
     *
     * @param params     统一参数
     * @param categoryId 分类id
     * @return
     */
    Page<AttrRespVO> queryAttrPage(Map<String, String> params, Long categoryId);

    /**
     * 获取属性的详细信息
     *
     * @param attrId 属性id
     * @return
     */
    AttrRespVO getInfoById(Long attrId);

    /**
     * 修改属性信息
     *
     * @param attrVO 数据
     */
    void updateAttr(AttrVO attrVO);

    /**
     * 获取属性分组关联的属性集合
     *
     * @param attrGroupId 属性分组id
     * @return
     */
    List<Attr> getGroupRelationAttr(Long attrGroupId);

    /**
     * 删除属性与分组的关联关系
     *
     * @param attrGroupRelationVOS 关系数组
     */
    void deleteRelation(AttrGroupRelationVO... attrGroupRelationVOS);

    /**
     * 获取属性分组没有关联的属性列表
     *
     * @param params
     * @param attrGroupId
     * @return
     */
    Page<Attr> getNoRelationAttr(Map<String, Object> params, Long attrGroupId);

    /**
     * 获取分组下的所有属性
     *
     * @param attrGroupId
     * @return
     */
    List<Attr> getRelationAttr(Long attrGroupId);

    /**
     * 筛选出可以检索的属性
     *
     * @param attrIds
     * @return
     */
    List<Long> selectSearchAttrIds(List<Long> attrIds);
}
