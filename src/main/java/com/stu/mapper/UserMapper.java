package com.stu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stu.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM user WHERE openid = #{openid} AND deleted = 0")
    User selectByOpenid(@Param("openid") String openid);
}