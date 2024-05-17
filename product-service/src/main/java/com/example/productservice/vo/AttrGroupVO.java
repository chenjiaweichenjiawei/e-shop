package com.example.productservice.vo;

import lombok.Data;

import java.util.List;

/**
 * @author CJW
 * @since 2024/5/9
 */
@Data
public class AttrGroupVO {
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    /**
     * 所属分类路径
     */
    private List<Long> catelogPath;
}
