package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.AttrGroupMapper;
import com.example.productservice.po.Attr;
import com.example.productservice.po.AttrGroup;
import com.example.productservice.service.AttrGroupService;
import com.example.productservice.service.AttrService;
import com.example.productservice.service.CategoryService;
import com.example.productservice.vo.AttrGroupVO;
import com.example.productservice.vo.AttrGroupWithAttrsVO;
import com.example.productservice.vo.skuItemvo.SpuItemAttrGroupVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 属性分组 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2023-11-21
 */
@Service
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupMapper, AttrGroup> implements AttrGroupService {
    @Autowired
    private AttrGroupMapper attrGroupMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrService attrService;

    @Override
    public Page<AttrGroup> getByCatId(Long catId, Map<String, String> paramMap) {
        long currentPage = Long.parseLong(paramMap.get("currentPage"));
        long pageSize = Long.parseLong(paramMap.get("pageSize"));
        String key = paramMap.get("key");
        Page<AttrGroup> attrGroupPage = new Page<>(currentPage, pageSize);
        LambdaQueryWrapper<AttrGroup> wrapper = new LambdaQueryWrapper<>();
        if (catId != 0) {
            wrapper.eq(AttrGroup::getCatelogId, catId);
        }
        wrapper.and(nextWrapper -> {
            nextWrapper.like(key != null, AttrGroup::getAttrGroupId, key).or().like(key != null, AttrGroup::getAttrGroupName, key);
        });
        attrGroupMapper.selectPage(attrGroupPage, wrapper);
        return attrGroupPage;
    }

    @Override
    public AttrGroupVO getInfoById(Long attrGroupId) {
        AttrGroup attrGroup = this.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        List<Long> path = categoryService.getPathByCatId(catelogId);
        AttrGroupVO attrGroupVO = new AttrGroupVO();
        BeanUtils.copyProperties(attrGroup, attrGroupVO);
        attrGroupVO.setCatelogPath(path);
        return attrGroupVO;
    }

    @Override
    public List<AttrGroupWithAttrsVO> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        List<AttrGroup> attrGroupList = this.list(new LambdaQueryWrapper<AttrGroup>().eq(AttrGroup::getCatelogId, catelogId));
        return attrGroupList.stream().map(item -> {
            //查出当前分类下的所有属性分组
            AttrGroupWithAttrsVO attrGroupWithAttrsVo = new AttrGroupWithAttrsVO();
            BeanUtils.copyProperties(item, attrGroupWithAttrsVo);//复制属性(两个实体类中属性名必须一致)
            //查出每个属性分组下的所有属性
            List<Attr> attrs = attrService.getRelationAttr(attrGroupWithAttrsVo.getAttrGroupId());
            if (attrs != null) {
                attrGroupWithAttrsVo.setAttrs(attrs);
            }
            return attrGroupWithAttrsVo;
        }).filter(attrVo -> attrVo.getAttrs() != null && attrVo.getAttrs().size() > 0).collect(Collectors.toList());
    }

    @Override
    public List<SpuItemAttrGroupVO> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        return attrGroupMapper.getAttrGroupWithAttrsBySpuId(spuId, catalogId);
    }
}
