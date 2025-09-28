package com.stu.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.stu.entity.AddressCreateDTO;
import com.stu.entity.UserAddress;
import com.stu.service.UserAddressService;
import com.stu.util.JwtUtil;
import com.stu.vo.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 创建地址
     */
    @PostMapping("/create")
    public Result create(@RequestBody @Valid AddressCreateDTO dto,
                         @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserId(authHeader);
        // 处理默认地址：如果本次创建的是默认，则先清除该用户已有默认
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            UpdateWrapper<UserAddress> uw = new UpdateWrapper<>();
            uw.eq("user_id", userId).set("is_default", 0);
            userAddressService.update(uw);
        }
        UserAddress entity = new UserAddress();
        entity.setUserId(userId);
        entity.setAddressLabel(dto.getAddressLabel());
        entity.setDetailAddress(dto.getDetailAddress());
        entity.setContactName(dto.getContactName());
        entity.setContactPhone(dto.getContactPhone());
        if (dto.getLat() != null && dto.getLng() != null) {
            // 直接存成简单 JSON 字符串
            entity.setLocation("{\"lat\":" + dto.getLat() + ",\"lng\":" + dto.getLng() + "}");
        }
        entity.setIsDefault(dto.getIsDefault());
        entity.setCreatedAt(new Date());
        entity.setUpdatedAt(new Date());
        userAddressService.save(entity);
        return Result.success().put("addressId", entity.getId());
    }

    /**
     * 地址列表
     */
    @GetMapping("/list")
    public Result list(@RequestHeader("Authorization") String authHeader) {
        Long userId = getUserId(authHeader);
        List<UserAddress> list = userAddressService.listByUserId(userId);
        return Result.success().put("addresses", list);
    }

    /**
     * 设置默认地址
     */
    @PostMapping("/default/{id}")
    public Result setDefault(@PathVariable Long id,
                             @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserId(authHeader);
        UserAddress address = userAddressService.getById(id);
        if (address == null) {
            return Result.error("地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            return Result.error("地址不属于当前用户");
        }
        UpdateWrapper<UserAddress> uw = new UpdateWrapper<>();
        uw.eq("user_id", userId).set("is_default", 0);
        userAddressService.update(uw);
        address.setIsDefault(1);
        address.setUpdatedAt(new Date());
        userAddressService.updateById(address);
        return Result.success().put("addressId", id);
    }

    private Long getUserId(String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        return jwtUtil.getUserIdFromToken(token);
    }
}

