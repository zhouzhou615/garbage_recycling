package com.stu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.stu.entity.User;

public interface UserService extends IService<User> {
    User getByOpenid(String openid);
    User createOrUpdateUser(User user);
}