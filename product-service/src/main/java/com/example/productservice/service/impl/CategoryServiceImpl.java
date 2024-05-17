package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.CategoryBrandRelationMapper;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.po.Category;
import com.example.productservice.service.CategoryService;
import com.example.productservice.util.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品三级分类 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2023-11-09
 */
@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private CategoryBrandRelationMapper categoryBrandRelationMapper;

    @Cacheable(cacheNames = "category", key = "#root.method.name+':'+#root.args", sync = true)
    @Override
    public List<Category> getCategoryTree() {
        /*if (redisUtils.get("categories") != null) {
            return JSON.parseObject(redisUtils.get("categories"), new TypeReference<List<Category>>() {
            });
        }
        RLock lock = redissonClient.getLock("categories-lock");
        lock.lock();
        List<Category> list;
        try {
            //双重检查，当多个线程都判断没有缓存进入到这个位置时，前面的一个线程获取到数据放入缓存这些线程就可以直接获取缓存，
            // 不用执行这个代码块里的查询数据库代码
            if (redisUtils.get("categories") != null) {
                return JSON.parseObject(redisUtils.get("categories"), new TypeReference<List<Category>>() {
                });
            }*/
        log.info("category查询了数据库");
        //查询全部的分类
        List<Category> all = categoryMapper.selectList(null);
        List<Category> list = all.stream()
                //获取所有一级分类，并递归查询子分类数据放入
                .filter(category -> category.getParentCid() == 0)
                //获取子分类结构树数据
                .map(parentCategory -> {
                    parentCategory.setChildrenCategories(getChildrenCategoryTree(parentCategory, all));
                    return parentCategory;
                })
                //按照sort字段递增排序
                .sorted(Comparator.comparingInt(Category::getSort)).collect(Collectors.toList());
       /* //缓存
        redisUtils.set("categories", JSON.toJSONString(list));*/
        return list;
    }

    private List<Category> getChildrenCategoryTree(Category parentCategory, List<Category> all) {
        return all.stream()
                //查出下一级的分类
                .filter(category -> category.getParentCid().equals(parentCategory.getCatId()))
                //使用递归查询到接下去所有级别的分类
                .map(category -> {
                    category.setChildrenCategories(getChildrenCategoryTree(category, all));
                    return category;
                })
                //按照sort字段递增排序
                .sorted(Comparator.comparingInt(Category::getSort)).collect(Collectors.toList());
    }

    @Override
    public void deleteByIds(Long[] catIds) {
        //检查是否被其他分类引用，被引用不能删除
        categoryMapper.deleteBatchIds(Arrays.asList(catIds));
    }

    @Cacheable(cacheNames = "category", key = "#root.method.name+':'+#root.args", sync = true)
    @Override
    public List<Long> getPathByCatId(Long catId) {
        log.info("path查询了数据库");
        System.out.println();
        List<Long> path = new ArrayList<>();
        List<Long> path1 = getPath(catId, path);
        Collections.reverse(path1);
        return path1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateDetail(Category category) {
        //更新分类数据
        this.updateById(category);
        //更新分类品牌对应数据
        categoryBrandRelationMapper.updateCategoryName(category.getCatId(), category.getName());
    }

    private List<Long> getPath(Long catId, List<Long> list) {
        list.add(catId);
        Category byId = this.getById(catId);
        if (byId.getParentCid() != 0) {
            getPath(byId.getParentCid(), list);
        }
        return list;
    }
}
