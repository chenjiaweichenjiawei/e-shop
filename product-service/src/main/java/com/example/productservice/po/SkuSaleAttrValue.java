package com.example.productservice.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * sku销售属性&值
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_sku_sale_attr_value")
public class SkuSaleAttrValue implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * sku_id
     */
    private Long skuId;

    /**
     * attr_id
     */
    private Long attrId;

    /**
     * 销售属性名
     */
    private String attrName;

    /**
     * 销售属性值
     */
    private String attrValue;

    /**
     * 顺序
     */
    private Integer attrSort;


}
