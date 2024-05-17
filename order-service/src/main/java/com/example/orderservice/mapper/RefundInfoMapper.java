package com.example.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orderservice.po.RefundInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 退款信息 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface RefundInfoMapper extends BaseMapper<RefundInfo> {

}
