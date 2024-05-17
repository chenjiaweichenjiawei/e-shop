package com.example.productservice.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.productservice.validation.AddBrand;
import com.example.productservice.validation.UpdateBrand;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * <p>
 * 品牌
 * </p>
 *
 * @author CJW
 * @since 2023-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_brand")
public class Brand implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId(value = "brand_id", type = IdType.AUTO)
    @NotNull(message = "品牌id不能为空", groups = UpdateBrand.class)
    @Null(message = "不能指定品牌id", groups = AddBrand.class)
    private Long brandId;
    /**
     * 品牌名
     */
    @Length(min = 1, message = "品牌名称至少一个字符", groups = UpdateBrand.class)
    @NotBlank(message = "品牌名称不能为空", groups = AddBrand.class)
    private String name;

    /**
     * 品牌logo地址
     */
    @URL(message = "logo地址必须为一个合法的访问地址", groups = {AddBrand.class, UpdateBrand.class})
    @NotBlank(message = "logo地址不能为空", groups = {AddBrand.class})
    private String logo;

    /**
     * 介绍
     */
    private String description;

    /**
     * 显示状态[0-不显示；1-显示]
     */
    private Integer showStatus;

    /**
     * 检索首字母
     */
    private String firstLetter;

    /**
     * 排序
     */
    private Integer sort;


}
