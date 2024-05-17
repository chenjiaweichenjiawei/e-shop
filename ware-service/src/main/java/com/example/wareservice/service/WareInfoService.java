package com.example.wareservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.wareservice.po.WareInfo;
import com.example.wareservice.vo.FareVO;

import java.util.Map;

/**
 * <p>
 * 仓库信息 服务类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
public interface WareInfoService extends IService<WareInfo> {
    /**
     * 获取仓库信息
     *
     * @param params
     * @return
     */
    Page<WareInfo> queryPage(Map<String, Object> params);

    /**
     * 获取运费
     *
     * @param addrId
     * @return
     */
    FareVO getFare(Long addrId);
}
