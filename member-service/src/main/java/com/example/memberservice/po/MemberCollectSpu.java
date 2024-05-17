package com.example.memberservice.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 会员收藏的商品
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("ums_member_collect_spu")
public class MemberCollectSpu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * spu_id
     */
    private Long spuId;

    /**
     * spu_name
     */
    private String spuName;

    /**
     * spu_img
     */
    private String spuImg;

    /**
     * create_time
     */
    private LocalDateTime createTime;


}
