// src/main/java/com/stu/controller/UserCenterController.java
package com.stu.controller;

import com.stu.entity.Address;
import com.stu.entity.User;
import com.stu.service.AddressService;
import com.stu.service.UserService;
import com.stu.util.JwtUtil;
import com.stu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/center")
public class UserCenterController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result getUserInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        return Result.success(result);

    }

    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result updateUserInfo(@RequestBody User user,
                                 @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 仅允许更新部分字段
        User existing = userService.getById(userId);
        existing.setNickname(user.getNickname())
                .setAvatarUrl(user.getAvatarUrl())
                .setIdentityType(user.getIdentityType())
                .setSchoolName(user.getSchoolName())
                .setDepartment(user.getDepartment())
                .setExtendedInfo(user.getExtendedInfo());

        userService.updateById(existing);
        return Result.success(Map.of("message", "信息更新成功"));
    }

    /**
     * 获取用户地址列表
     */
    @GetMapping("/addresses")
    public Result getUserAddresses(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<Address> addresses = addressService.getUserAddresses(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("addresses", addresses);
        return Result.success(result);
    }

    /**
     * 添加新地址
     */
    @PostMapping("/addresses")
    public Result addAddress(@RequestBody Address address,
                             @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return addressService.addAddress(address, userId);
    }

    /**
     * 更新地址
     */
    @PutMapping("/addresses")
    public Result updateAddress(@RequestBody Address address,
                                @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return addressService.updateAddress(address, userId);
    }

    /**
     * 删除地址
     */
    @DeleteMapping("/addresses/{id}")
    public Result deleteAddress(@PathVariable Long id,
                                @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return addressService.deleteAddress(id, userId);
    }

    /**
     * 设置默认地址
     */
    @PostMapping("/addresses/{id}/default")
    public Result setDefaultAddress(@PathVariable Long id,
                                    @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return addressService.setDefaultAddress(id, userId);
    }
}