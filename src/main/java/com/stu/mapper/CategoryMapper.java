package com.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stu.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}