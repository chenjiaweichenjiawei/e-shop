package com.example.couponservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.couponservice.mapper.HomeSubjectMapper;
import com.example.couponservice.po.HomeSubject;
import com.example.couponservice.service.HomeSubjectService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 首页专题表【jd首页下面很多专题，每个专题链接新的页面，展示专题商品信息】 服务实现类
 * </p>
 *
 * @author CJW
 * @since 2024-05-10
 */
@Service
public class HomeSubjectServiceImpl extends ServiceImpl<HomeSubjectMapper, HomeSubject> implements HomeSubjectService {

}
