package com.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stu.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    // 继承 BaseMapper 即可
}

