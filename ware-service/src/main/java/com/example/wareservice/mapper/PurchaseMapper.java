package com.example.wareservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.wareservice.po.Purchase;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 采购信息 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Mapper
public interface PurchaseMapper extends BaseMapper<Purchase> {

}
