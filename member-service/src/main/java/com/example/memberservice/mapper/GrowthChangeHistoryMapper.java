package com.example.memberservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.memberservice.po.GrowthChangeHistory;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 成长值变化历史记录 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface GrowthChangeHistoryMapper extends BaseMapper<GrowthChangeHistory> {

}
