// src/main/java/com/stu/service/AddressService.java
package com.stu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stu.entity.Address;
import com.stu.vo.Result;

import java.util.List;

public interface AddressService extends IService<Address> {
    Result addAddress(Address address, Long userId);
    Result updateAddress(Address address, Long userId);
    Result deleteAddress(Long addressId, Long userId);
    Result setDefaultAddress(Long addressId, Long userId);
    List<Address> getUserAddresses(Long userId);
    Address getDefaultAddress(Long userId);
}