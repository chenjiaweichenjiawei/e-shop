package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.constant.ProductConstant;
import com.example.common.util.PageUtils;
import com.example.productservice.mapper.AttrAttrgroupRelationMapper;
import com.example.productservice.mapper.AttrGroupMapper;
import com.example.productservice.mapper.AttrMapper;
import com.example.productservice.mapper.CategoryMapper;
import com.example.productservice.po.Attr;
import com.example.productservice.po.AttrAttrgroupRelation;
import com.example.productservice.po.AttrGroup;
import com.example.productservice.po.Category;
import com.example.productservice.service.AttrService;
import com.example.productservice.service.CategoryService;
import com.example.productservice.vo.AttrGroupRelationVO;
import com.example.productservice.vo.AttrRespVO;
import com.example.productservice.vo.AttrVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 商品属性 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-03-03
 */
@Service
public class AttrServiceImpl extends ServiceImpl<AttrMapper, Attr> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationMapper attrAttrgroupRelationMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private AttrGroupMapper attrGroupMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrMapper attrMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveDetail(AttrVO attrVO) {
        //保存基本数据
        Attr attr = new Attr();
        BeanUtils.copyProperties(attrVO, attr);
        this.save(attr);
        //保存关联关系
        AttrAttrgroupRelation attrAttrgroupRelation = new AttrAttrgroupRelation();
        attrAttrgroupRelation.setAttrGroupId(attrVO.getAttrGroupId());
        attrAttrgroupRelation.setAttrId(attrVO.getAttrId());
        attrAttrgroupRelationMapper.insert(attrAttrgroupRelation);
    }

    @Override
    public Page<AttrRespVO> queryAttrPage(Map<String, String> params, Long categoryId) {
        LambdaQueryWrapper<Attr> queryWrapper = new LambdaQueryWrapper<>();
        if (categoryId != 0) {
            queryWrapper.eq(Attr::getCatelogId, categoryId);
        }
        //处理查询条件
        String key = params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and(lambdaQueryWrapper -> {
                lambdaQueryWrapper.eq(Attr::getAttrId, key).or().like(Attr::getAttrName, key);
            });
        }
        long currentPage = Long.parseLong(params.get("currentPage"));
        long pageSize = Long.parseLong(params.get("pageSize"));
        Page<Attr> attrPage = this.page(new Page<>(currentPage, pageSize), queryWrapper);

        List<Attr> records = attrPage.getRecords();
        List<AttrRespVO> attrRespVOS = records.stream().map(attr -> {
            AttrRespVO attrRespVo = new AttrRespVO();
            //复制一致的属性
            BeanUtils.copyProperties(attr, attrRespVo);
            //增加分类名称
            Category category = categoryMapper.selectById(attr.getCatelogId());
            attrRespVo.setCategoryName(category.getName());
            //增加分组名称
            LambdaQueryWrapper<AttrAttrgroupRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(AttrAttrgroupRelation::getAttrId, attr.getAttrId());
            AttrAttrgroupRelation attrAttrgroupRelation = attrAttrgroupRelationMapper.selectOne(lambdaQueryWrapper);
            if (attrAttrgroupRelation != null) {
                AttrGroup attrGroup = attrGroupMapper.selectById(attrAttrgroupRelation.getAttrGroupId());
                attrRespVo.setGroupName(attrGroup.getAttrGroupName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        Page<AttrRespVO> attrRespVoPage = new Page<>();
        BeanUtils.copyProperties(attrPage, attrRespVoPage, "records");
        attrRespVoPage.setRecords(attrRespVOS);
        return attrRespVoPage;
    }

    @Override
    public AttrRespVO getInfoById(Long attrId) {
        Attr attr = this.getById(attrId);
        AttrRespVO attrRespVo = new AttrRespVO();
        BeanUtils.copyProperties(attr, attrRespVo);
        //设置分组信息
        LambdaQueryWrapper<AttrAttrgroupRelation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AttrAttrgroupRelation::getAttrId, attrId);
        AttrAttrgroupRelation attrAttrgroupRelation = attrAttrgroupRelationMapper.selectOne(lambdaQueryWrapper);
        if (attrAttrgroupRelation != null) {
            attrRespVo.setAttrGroupId(attrAttrgroupRelation.getAttrGroupId());
            AttrGroup attrGroup = attrGroupMapper.selectById(attrAttrgroupRelation.getAttrGroupId());
            if (attrGroup != null) {
                attrRespVo.setGroupName(attrGroup.getAttrGroupName());
            }
        }
        //设置分类信息
        Long catelogId = attr.getCatelogId();
        List<Long> path = categoryService.getPathByCatId(catelogId);
        attrRespVo.setCatelogPath(path);
        Category category = categoryService.getById(catelogId);
        if (category != null) {
            attrRespVo.setCategoryName(category.getName());
        }
        return attrRespVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAttr(AttrVO attrVO) {
        Attr attr = new Attr();
        BeanUtils.copyProperties(attrVO, attr);
        this.updateById(attr);

        //修改分组关联
        AttrAttrgroupRelation attrAttrgroupRelation = new AttrAttrgroupRelation();
        attrAttrgroupRelation.setAttrGroupId(attrVO.getAttrGroupId());
        attrAttrgroupRelation.setAttrId(attr.getAttrId());
        //统计attr_id的关联属性,如果没有初始分组,则进行添加操作;有则进行修改操作
        Long count = attrAttrgroupRelationMapper.selectCount(new LambdaQueryWrapper<AttrAttrgroupRelation>().eq(AttrAttrgroupRelation::getAttrId, attrVO.getAttrId()));
        if (count > 0) {
            attrAttrgroupRelationMapper.update(attrAttrgroupRelation, new LambdaQueryWrapper<AttrAttrgroupRelation>().eq(AttrAttrgroupRelation::getAttrId, attr.getAttrId()));
        } else {
            attrAttrgroupRelationMapper.insert(attrAttrgroupRelation);
        }
    }

    @Override
    public List<Attr> getGroupRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelation> attrAttrgroupRelations = attrAttrgroupRelationMapper.selectList(new LambdaQueryWrapper<AttrAttrgroupRelation>().eq(AttrAttrgroupRelation::getAttrGroupId, attrGroupId));
        if (attrAttrgroupRelations != null && attrAttrgroupRelations.size() > 0) {
            List<Long> attrIds = attrAttrgroupRelations.stream().map(AttrAttrgroupRelation::getAttrId).collect(Collectors.toList());
            if (attrIds.size() == 0) {
                return null;
            } else {
                return this.listByIds(attrIds);
            }
        } else {
            return null;
        }
    }

    @Override
    public void deleteRelation(AttrGroupRelationVO... attrGroupRelationVOS) {
        List<AttrAttrgroupRelation> vos = Arrays.stream(attrGroupRelationVOS).map((item) -> {
            AttrAttrgroupRelation entity = new AttrAttrgroupRelation();
            BeanUtils.copyProperties(item, entity);
            return entity;
        }).collect(Collectors.toList());
        attrAttrgroupRelationMapper.deleteBatchRelation(vos);
    }

    @Override
    public Page<Attr> getNoRelationAttr(Map<String, Object> params, Long attrGroupId) {
        //获取当前分组所属的分类
        AttrGroup attrGroupEntity = attrGroupMapper.selectById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //找到该分类下其他分组所关联的所有属性
        List<AttrGroup> attrGroups = attrGroupMapper.selectList(new LambdaQueryWrapper<AttrGroup>().eq(AttrGroup::getCatelogId, catelogId));
        List<Long> collect = attrGroups.stream().map(AttrGroup::getAttrGroupId).collect(Collectors.toList());
        List<AttrAttrgroupRelation> relations = attrAttrgroupRelationMapper.selectList(new LambdaQueryWrapper<AttrAttrgroupRelation>().in(AttrAttrgroupRelation::getAttrGroupId, collect));
        List<Long> attrIds = relations.stream().map(AttrAttrgroupRelation::getAttrId).collect(Collectors.toList());
        //过滤掉当前分类的属性中已被使用的属性
        LambdaQueryWrapper<Attr> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Attr::getCatelogId, catelogId).eq(Attr::getAttrType, ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds.size() > 0) {
            queryWrapper.notIn(Attr::getAttrId, attrIds);
        }
        String key = (String) params.get("key");
        if (key != null && key.length() != 0) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq(Attr::getAttrId, key).or().like(Attr::getAttrName, key);
            });
        }
        return this.page(PageUtils.getPage(params), queryWrapper);
    }

    @Override
    public List<Attr> getRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelation> relations = attrAttrgroupRelationMapper.selectList(new LambdaQueryWrapper<AttrAttrgroupRelation>().eq(AttrAttrgroupRelation::getAttrGroupId, attrGroupId));
        if (relations != null && relations.size() > 0) {
            List<Long> attrIds = relations.stream().map(AttrAttrgroupRelation::getAttrId).collect(Collectors.toList());
            return this.listByIds(attrIds);
        } else {
            return null;
        }
    }

    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {
        return attrMapper.selectSearchAttrIds(attrIds);
    }
}
