package com.example.productservice.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.example.productservice.po.Attr;
import lombok.Data;

import java.util.List;

/**
 * @author CJW
 */
@Data
public class AttrGroupWithAttrsVO {
    /**
     * 分组id
     */
    @TableId
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

    private List<Attr> attrs;
}
