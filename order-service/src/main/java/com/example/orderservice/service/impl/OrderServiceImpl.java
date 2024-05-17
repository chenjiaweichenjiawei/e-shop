package com.example.orderservice.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.orderservice.constant.OrderStatusEnum;
import com.example.orderservice.dto.OrderCreateDTO;
import com.example.orderservice.feign.MemberServiceFeignClient;
import com.example.orderservice.feign.ProductServiceFeignClient;
import com.example.orderservice.feign.WareServiceFeignClient;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.po.Order;
import com.example.orderservice.po.OrderItem;
import com.example.orderservice.service.OrderItemService;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private MemberServiceFeignClient memberServiceFeignClient;
    @Autowired
    private ProductServiceFeignClient productServiceFeignClient;
    @Autowired
    private WareServiceFeignClient wareServiceFeignClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private OrderItemService orderItemService;

    @Override
    public OrderConfirmVO confirm(OrderConfirmVO orderConfirmVO, MemberRespVO memberRespVO) {
        OrderConfirmVO confirmVo = new OrderConfirmVO();

        //1.远程客户地址列表
        List<MemberAddressVO> address = memberServiceFeignClient.getAddress(memberRespVO.getId());
        confirmVo.setAddress(address);

        //2.远程购物车所有选中的购物项
        List<OrderItemVO> items = productServiceFeignClient.getCurrentUserCartItems(memberRespVO.getId()).getData();
        confirmVo.setItems(items);

        List<OrderItemVO> orderItemVOS = confirmVo.getItems();
        List<Long> collect = orderItemVOS.stream().map(OrderItemVO::getSkuId).collect(Collectors.toList());
        List<SkuStockVO> skuStockVOS = wareServiceFeignClient.getSkuHasStock(collect).getData();
        if (skuStockVOS != null) {
            Map<Long, Boolean> collect1 = skuStockVOS.stream().collect(
                    Collectors.toMap(SkuStockVO::getSkuId,
                            SkuStockVO::getHasStock));
            confirmVo.setStocks(collect1);
        }

        //3.查询用户积分
        Integer integration = memberRespVO.getIntegration();
        confirmVo.setIntegration(integration);
        //4.防重复令牌提交
        String token = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set("order:token:" + memberRespVO.getId(), token, 30, TimeUnit.MINUTES);
        confirmVo.setToken(token);

        return confirmVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SubmitOrderResponseVO submitOrder(OrderSubmitVO orderSubmitVO, MemberRespVO memberRespVO) {
        SubmitOrderResponseVO responseVo = new SubmitOrderResponseVO();
        responseVo.setCode(0);
        //1.验证令牌(令牌的对比和删除必须保证原子性)
        //如果调用get方法与传入的val相同，调用del方法，不相同返回0(0失败，1成功)
        String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        String orderToken = orderSubmitVO.getOrderToken();
        //使用Lua脚本
        //Lua脚本里使用的是长度为1的List，所以需要asList
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Arrays.asList("order:token:" + memberRespVO.getId()), orderToken);
        if (result == 0) {
            //失败
            responseVo.setCode(1);
            return responseVo;
        } else {
            //成功
            //1.创建订单
            OrderCreateDTO order = createOrder(orderSubmitVO, memberRespVO);
            //2.验价
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = orderSubmitVO.getPayPrice();
            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01) {
                //成功
                //3.保存订单
                saveOrder(order);
                //4.锁定库存,有异常回滚数据
                //订单号,skuId,skuName,数量
                //远程锁库存
                return responseVo;
            } else {
                responseVo.setCode(2);
                return responseVo;
            }
        }
    }

    /**
     * 保存订单
     */
    private void saveOrder(OrderCreateDTO order) {
        Order orderEntity = order.getOrder();
        orderEntity.setModifyTime(LocalDateTime.now());
        this.save(orderEntity);
        List<OrderItem> orderItems = order.getItems();
        orderItemService.saveBatch(orderItems);
    }

    /**
     * 创建订单
     */
    private OrderCreateDTO createOrder(OrderSubmitVO orderSubmitVO, MemberRespVO memberRespVO) {
        OrderCreateDTO orderCreateTo = new OrderCreateDTO();
        //生成订单号
        String orderSn = IdWorker.getTimeId();
        //执行创建订单方法
        Order order = buildOrder(orderSn, orderSubmitVO, memberRespVO);
        //得到所有订单项
        List<OrderItem> itemEntities = buildOrderItems(orderSn);
        //验价
        computePrice(order, itemEntities);
        orderCreateTo.setItems(itemEntities);
        orderCreateTo.setOrder(order);
        return orderCreateTo;
    }

    /**
     * 构建订单
     */
    private Order buildOrder(String orderSn, OrderSubmitVO orderSubmitVO, MemberRespVO memberRespVO) {
        Order entity = new Order();
        entity.setOrderSn(orderSn);
        entity.setMemberId(memberRespVO.getId());
        //获取收货地址信息
        FareVO data = wareServiceFeignClient.getFare(orderSubmitVO.getAddrId()).getData();
        //设置运费信息
        entity.setFreightAmount(data.getFare());
        //设置收货信息
        entity.setReceiverCity(data.getAddress().getCity());
        entity.setReceiverDetailAddress(data.getAddress().getDetailAddress());
        entity.setReceiverName(data.getAddress().getName());
        entity.setBillReceiverPhone(data.getAddress().getPhone());
        entity.setReceiverPostCode(data.getAddress().getPostCode());
        entity.setReceiverProvince(data.getAddress().getProvince());
        entity.setReceiverRegion(data.getAddress().getRegion());
        //设置订单状态
        entity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        entity.setAutoConfirmDay(7);
        //删除状态,0未删除
        entity.setDeleteStatus(0);
        return entity;
    }

    /**
     * 构建所有订单项
     */
    private List<OrderItem> buildOrderItems(String orderSn) {
        //获取所有订单项,最后确定每个购物项的价格
        Long userId = null;
        List<OrderItemVO> currentUserCartItems = productServiceFeignClient.getCurrentUserCartItems(userId).getData();
        if (currentUserCartItems != null && currentUserCartItems.size() > 0) {
            List<OrderItem> collect = currentUserCartItems.stream().map(cartItem -> {
                //构建订单项数据
                OrderItem itemEntity = buildOrderItem(cartItem);
                itemEntity.setOrderSn(orderSn);
                return itemEntity;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    /**
     * 构建某一个订单项
     */
    private OrderItem buildOrderItem(OrderItemVO cartItem) {
        OrderItem itemEntity = new OrderItem();
        //1.订单信息:订单号
        //2.商品spu信息
        Long skuId = cartItem.getSkuId();
        SpuInfoVO data = productServiceFeignClient.getSpuInfoBySkuId(skuId).getData();
        itemEntity.setSpuId(data.getId());
        itemEntity.setSpuBrand(data.getBrandId().toString());
        itemEntity.setSpuName(data.getSpuName());
        itemEntity.setCategoryId(data.getCatalogId());
        //3.商品sku信息
        itemEntity.setSkuId(skuId);
        itemEntity.setSkuName(cartItem.getTitle());
        itemEntity.setSkuPic(cartItem.getImage());
        itemEntity.setSkuPrice(cartItem.getPrice());
        String skuAttr = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");
        itemEntity.setSkuAttrsVals(skuAttr);
        itemEntity.setSkuQuantity(cartItem.getCount());
        //4.优惠信息
        itemEntity.setPromotionAmount(new BigDecimal("0"));
        itemEntity.setCouponAmount(new BigDecimal("0"));
        itemEntity.setIntegrationAmount(new BigDecimal("0"));
        //5.积分信息
        itemEntity.setGiftGrowth(cartItem.getPrice().intValue() * cartItem.getCount());
        itemEntity.setGiftIntegration(cartItem.getPrice().intValue() * cartItem.getCount());
        //6.订单项价格信息
        //实际金额
        BigDecimal orgin = itemEntity.getSkuPrice().multiply(new BigDecimal(itemEntity.getSkuQuantity().toString()));
        BigDecimal subtract = orgin.subtract(itemEntity.getPromotionAmount()).subtract(itemEntity.getCouponAmount()).subtract(itemEntity.getIntegrationAmount());
        itemEntity.setRealAmount(subtract);
        return itemEntity;
    }

    /**
     * 验价方法
     */
    private void computePrice(Order order, List<OrderItem> itemEntities) {
        //订单总额,计算各种优惠的总额,计算积分和成长值总额
        BigDecimal couponAmount = new BigDecimal("0.0");
        BigDecimal promotionAmount = new BigDecimal("0.0");
        BigDecimal integrationAmount = new BigDecimal("0.0");
        Integer integration = 0;
        Integer growth = 0;
        BigDecimal total = new BigDecimal("0.0");
        for (OrderItem entity : itemEntities) {
            couponAmount = couponAmount.add(entity.getCouponAmount());
            promotionAmount = promotionAmount.add(entity.getPromotionAmount());
            integrationAmount = integrationAmount.add(entity.getIntegrationAmount());
            growth += entity.getGiftGrowth();
            integration += entity.getGiftIntegration();
            total = total.add(entity.getRealAmount());
        }
        order.setTotalAmount(total);
        order.setPayAmount(total.add(order.getFreightAmount()));
        order.setPromotionAmount(promotionAmount);
        order.setCouponAmount(couponAmount);
        order.setIntegrationAmount(integrationAmount);
        order.setGrowth(growth);
        order.setIntegration(integration);
    }


}
