package com.stu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stu.entity.UserAddress;
import com.stu.mapper.UserAddressMapper;
import com.stu.service.UserAddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {
    private static final Logger log = LoggerFactory.getLogger(UserAddressServiceImpl.class);
    @Override
    public boolean validateUserAddress(Long addressId, Long userId) {
        if (addressId == null || userId == null) {
            log.debug("validateUserAddress 参数为空 addressId={}, userId={}", addressId, userId);
            return false;
        }
        QueryWrapper<UserAddress> qw = new QueryWrapper<>();
        qw.eq("id", addressId)
          .eq("user_id", userId);
        long count = this.count(qw);
        log.debug("地址校验 addressId={}, userId={}, count={}", addressId, userId, count);
        return count == 1;
    }

    @Override
    public List<UserAddress> listByUserId(Long userId) {
        return this.lambdaQuery().eq(UserAddress::getUserId, userId).orderByDesc(UserAddress::getId).list();
    }
}
