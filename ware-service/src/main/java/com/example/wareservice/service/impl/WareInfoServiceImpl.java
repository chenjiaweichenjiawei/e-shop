package com.example.wareservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.util.PageUtils;
import com.example.wareservice.feign.MemberServiceFeignClient;
import com.example.wareservice.mapper.WareInfoMapper;
import com.example.wareservice.po.WareInfo;
import com.example.wareservice.service.WareInfoService;
import com.example.wareservice.vo.FareVO;
import com.example.wareservice.vo.MemberAddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 仓库信息 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class WareInfoServiceImpl extends ServiceImpl<WareInfoMapper, WareInfo> implements WareInfoService {
    @Autowired
    private MemberServiceFeignClient memberServiceFeignClient;

    @Override
    public Page<WareInfo> queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareInfo> queryWrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.eq(WareInfo::getId, key)
                    .or().like(WareInfo::getName, key)
                    .or().like(WareInfo::getAddress, key)
                    .or().like(WareInfo::getAreacode, key);
        }
        return this.page(PageUtils.getPage(params), queryWrapper);
    }

    @Override
    public FareVO getFare(Long addrId) {
        FareVO fareVo = new FareVO();
        MemberAddressVO data = memberServiceFeignClient.addrInfo(addrId).getData();
        if (data != null) {
            String phone = data.getPhone();
            String substring = phone.substring(phone.length() - 2, phone.length());
            fareVo.setAddress(data);
            fareVo.setFare(new BigDecimal(substring));
            return fareVo;
        }
        return null;
    }
}
