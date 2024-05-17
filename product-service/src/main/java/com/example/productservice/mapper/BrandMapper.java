package com.example.productservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.productservice.po.Brand;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 品牌 Mapper 接口
 * </p>
 *
 * @author CJW
 * @since 2023-11-14
 */
@Mapper
public interface BrandMapper extends BaseMapper<Brand> {

}
