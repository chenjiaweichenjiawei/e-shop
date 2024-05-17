package com.example.orderservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orderservice.mapper.OrderSettingMapper;
import com.example.orderservice.po.OrderSetting;
import com.example.orderservice.service.OrderSettingService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单配置信息 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Service
public class OrderSettingServiceImpl extends ServiceImpl<OrderSettingMapper, OrderSetting> implements OrderSettingService {

}
