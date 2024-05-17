package com.example.memberservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.memberservice.po.MemberStatisticsInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 会员统计信息 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2024-05-14
 */
@Mapper
public interface MemberStatisticsInfoMapper extends BaseMapper<MemberStatisticsInfo> {

}
