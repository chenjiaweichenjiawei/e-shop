package com.example.productservice.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 商品评价
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pms_spu_comment")
public class SpuComment implements Serializable {

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
     * spu_id
     */
    private Long spuId;

    /**
     * 商品名字
     */
    private String spuName;

    /**
     * 会员昵称
     */
    private String memberNickName;

    /**
     * 星级
     */
    private Boolean star;

    /**
     * 会员ip
     */
    private String memberIp;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 显示状态[0-不显示，1-显示]
     */
    private Boolean showStatus;

    /**
     * 购买时属性组合
     */
    private String spuAttributes;

    /**
     * 点赞数
     */
    private Integer likesCount;

    /**
     * 回复数
     */
    private Integer replyCount;

    /**
     * 评论图片/视频[json数据；[{type:文件类型,url:资源路径}]]
     */
    private String resources;

    /**
     * 内容
     */
    private String content;

    /**
     * 用户头像
     */
    private String memberIcon;

    /**
     * 评论类型[0 - 对商品的直接评论，1 - 对评论的回复]
     */
    private Integer commentType;


}
