package com.example.productservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.productservice.mapper.AttrAttrgroupRelationMapper;
import com.example.productservice.po.AttrAttrgroupRelation;
import com.example.productservice.service.AttrAttrgroupRelationService;
import com.example.productservice.vo.AttrGroupRelationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 属性&属性分组关联 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-03-03
 */
@Service
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationMapper, AttrAttrgroupRelation> implements AttrAttrgroupRelationService {

    @Override
    public void saveBatch(List<AttrGroupRelationVO> vos) {
        List<AttrAttrgroupRelation> collect = vos.stream().map((item) -> {
            AttrAttrgroupRelation attrgroupRelation = new AttrAttrgroupRelation();
            BeanUtils.copyProperties(item, attrgroupRelation);
            return attrgroupRelation;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }
}
