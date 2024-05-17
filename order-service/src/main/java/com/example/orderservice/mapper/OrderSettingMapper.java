package com.example.orderservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.orderservice.po.OrderSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单配置信息 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface OrderSettingMapper extends BaseMapper<OrderSetting> {

}
