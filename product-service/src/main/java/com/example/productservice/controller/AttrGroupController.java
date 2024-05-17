package com.example.productservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.util.Result;
import com.example.productservice.po.Attr;
import com.example.productservice.po.AttrGroup;
import com.example.productservice.service.AttrAttrgroupRelationService;
import com.example.productservice.service.AttrGroupService;
import com.example.productservice.service.AttrService;
import com.example.productservice.vo.AttrGroupRelationVO;
import com.example.productservice.vo.AttrGroupVO;
import com.example.productservice.vo.AttrGroupWithAttrsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 属性分组 前端控制器
 * </p>
 *
 * @author CJW
 * @since 2023-11-21
 */
@RestController
@RequestMapping("/attrGroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    @GetMapping("/{catId}")
    public Result<Page<AttrGroup>> getByCatId(@PathVariable Long catId, @RequestParam Map<String, String> paramMap) {
        Page<AttrGroup> attrGroupPage = attrGroupService.getByCatId(catId, paramMap);
        return Result.success(attrGroupPage);
    }

    @GetMapping("/info/{attrGroupId}")
    public Result<AttrGroupVO> getInfoById(@PathVariable Long attrGroupId) {
        AttrGroupVO attrGroupVO = attrGroupService.getInfoById(attrGroupId);
        return Result.success(attrGroupVO);
    }

    @GetMapping("/{attrGroupId}/attr/relation")
    public Result<List<Attr>> attrRelation(@PathVariable("attrGroupId") Long attrGroupId) {
        List<Attr> attrList = attrService.getGroupRelationAttr(attrGroupId);
        return Result.success(attrList);
    }

    @GetMapping("/{attrGroupId}/attr/norelation")
    public Result<Page<Attr>> attrNoRelation(@PathVariable("attrGroupId") Long attrGroupId,
                                             @RequestParam Map<String, Object> params) {
        Page<Attr> page = attrService.getNoRelationAttr(params, attrGroupId);
        return Result.success(page);
    }

    @PostMapping("/attr/relation/delete")
    public Result<Void> deleteRelation(@RequestBody AttrGroupRelationVO[] attrGroupRelationVOS) {
        attrService.deleteRelation(attrGroupRelationVOS);
        return Result.success();
    }

    @PostMapping("/attr/relation")
    public Result<Void> addRelation(@RequestBody List<AttrGroupRelationVO> vos) {
        attrAttrgroupRelationService.saveBatch(vos);
        return Result.success();
    }

    @GetMapping("/{catelogId}/withattr")
    public Result<List<AttrGroupWithAttrsVO>> getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrsVO> vos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return Result.success(vos);
    }
}
