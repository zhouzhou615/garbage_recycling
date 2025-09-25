// src/main/java/com/stu/service/impl/AddressServiceImpl.java
package com.stu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stu.entity.Address;
import com.stu.mapper.AddressMapper;
import com.stu.service.AddressService;
import com.stu.vo.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    @Transactional
    public Result addAddress(Address address, Long userId) {
        address.setUserId(userId);
        address.setDeleted(0);
        address.setCreatedAt(new Date());
        address.setUpdatedAt(new Date());

        // 如果设置为默认地址，先取消该用户其他默认地址
        if (address.getIsDefault() == 1) {
            LambdaUpdateWrapper<Address> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Address::getUserId, userId)
                    .set(Address::getIsDefault, 0);
            baseMapper.update(null, updateWrapper);
        } else {
            // 如果是首次添加地址，自动设为默认
            long count = baseMapper.selectCount(new QueryWrapper<Address>()
                    .eq("user_id", userId)
                    .eq("deleted", 0));
            if (count == 0) {
                address.setIsDefault(1);
            }
        }

        baseMapper.insert(address);
        return Result.success(Map.of("message", "地址添加成功"));
    }

    @Override
    @Transactional
    public Result updateAddress(Address address, Long userId) {
        Address existing = baseMapper.selectById(address.getId());
        if (existing == null || !existing.getUserId().equals(userId)) {
            return Result.error("地址不存在或无权修改");
        }

        address.setUserId(userId);
        address.setUpdatedAt(new Date());

        if (address.getIsDefault() == 1) {
            LambdaUpdateWrapper<Address> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(Address::getUserId, userId)
                    .set(Address::getIsDefault, 0);
            baseMapper.update(null, updateWrapper);
        }

        baseMapper.updateById(address);
        // 修改第67行代码如下：
        return Result.success(Map.of("message", "地址更新成功"));

    }

    @Override
    @Transactional
    public Result deleteAddress(Long addressId, Long userId) {
        Address address = baseMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.error("地址不存在或无权删除");
        }

        // 如果删除的是默认地址，需要将第一个地址设为默认
        if (address.getIsDefault() == 1) {
            List<Address> otherAddresses = baseMapper.selectByUserId(userId);
            if (!otherAddresses.isEmpty()) {
                Address firstAddr = otherAddresses.get(0);
                firstAddr.setIsDefault(1);
                firstAddr.setUpdatedAt(new Date());
                baseMapper.updateById(firstAddr);
            }
        }

        baseMapper.deleteById(addressId);
        return Result.success(Map.of("message", "地址删除成功"));
    }

    @Override
    @Transactional
    public Result setDefaultAddress(Long addressId, Long userId) {
        Address address = baseMapper.selectById(addressId);
        if (address == null || !address.getUserId().equals(userId)) {
            return Result.error("地址不存在或无权操作");
        }

        // 先取消所有默认地址
        LambdaUpdateWrapper<Address> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Address::getUserId, userId)
                .set(Address::getIsDefault, 0);
        baseMapper.update(null, updateWrapper);

        // 设置当前地址为默认
        address.setIsDefault(1);
        address.setUpdatedAt(new Date());
        baseMapper.updateById(address);

        return Result.success(Map.of("message", "默认地址设置成功"));
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return baseMapper.selectByUserId(userId);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        return baseMapper.selectDefaultByUserId(userId);
    }
}