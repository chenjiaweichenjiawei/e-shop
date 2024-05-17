package com.example.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.po.CommentReplay;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品评价回复关系 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Mapper
public interface CommentReplayMapper extends BaseMapper<CommentReplay> {

}
