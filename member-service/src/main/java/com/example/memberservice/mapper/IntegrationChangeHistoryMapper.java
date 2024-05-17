package com.example.memberservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.memberservice.po.IntegrationChangeHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 积分变化历史记录 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface IntegrationChangeHistoryMapper extends BaseMapper<IntegrationChangeHistory> {

}
