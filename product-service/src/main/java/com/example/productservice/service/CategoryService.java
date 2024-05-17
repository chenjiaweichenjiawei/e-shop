package com.example.productservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.productservice.po.Category;

import java.util.List;

/**
 * <p>
 * 商品三级分类 服务类
 * </p>
 *
 * @author CJW
 * @since 2023-11-09
 */
public interface CategoryService extends IService<Category> {
    /**
     * 获取全部分类的树形结构数据
     *
     * @return
     */
    List<Category> getCategoryTree();

    /**
     * 删除分类
     *
     * @param catIds 分类id数组
     */
    void deleteByIds(Long[] catIds);

    /**
     * 获取分类的多级路径
     *
     * @param catId 分类id
     * @return
     */
    List<Long> getPathByCatId(Long catId);

    /**
     * 修改分类
     *
     * @param category 新数据
     */
    void updateDetail(Category category);
}
