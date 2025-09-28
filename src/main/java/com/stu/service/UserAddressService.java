package com.stu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stu.entity.UserAddress;

import java.util.List;

public interface UserAddressService extends IService<UserAddress> {
    /**
     * 校验地址是否存在并且属于指定用户
     *
     * @param addressId 地址ID
     * @param userId    用户ID
     * @return true 合法；false 不合法
     */
    boolean validateUserAddress(Long addressId, Long userId);

    /**
     * 新增：获取用户全部地址
     *
     * @param userId 用户ID
     * @return 用户的所有地址列表
     */
    List<UserAddress> listByUserId(Long userId);
}
