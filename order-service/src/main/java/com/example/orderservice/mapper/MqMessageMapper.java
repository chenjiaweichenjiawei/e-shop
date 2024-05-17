package com.example.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orderservice.po.MqMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface MqMessageMapper extends BaseMapper<MqMessage> {

}
