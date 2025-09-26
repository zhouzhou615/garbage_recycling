// UserService.java 添加方法
package com.stu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stu.entity.User;
import com.stu.vo.UserUpdateVO;

public interface UserService extends IService<User> {
    User getByOpenid(String openid);
    User createOrUpdateUser(User user);

    User completeUserInfo(Long userId, UserUpdateVO userUpdateVO);

    User getUserInfoById(Long userId);
}