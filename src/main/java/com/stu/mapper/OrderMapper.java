package com.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stu.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    // 可以添加自定义查询方法
}