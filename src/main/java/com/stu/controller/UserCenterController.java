package com.stu.controller;

import com.stu.entity.Address;
import com.stu.entity.User;
import com.stu.service.AddressService;
import com.stu.service.UserService;
import com.stu.util.JwtUtil;
import com.stu.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user/center")
@Tag(name = "个人中心接口", description = "用户信息管理和地址管理相关接口")
@SecurityRequirement(name = "bearerAuth") // 需JWT认证
public class UserCenterController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/info")
    @Operation(summary = "获取当前用户信息", description = "获取登录用户的基本信息（不含敏感数据）")
    public Result getUserInfo(
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        return Result.success(result);
    }

    @PutMapping("/info")
    @Operation(summary = "更新用户信息", description = "支持更新昵称、头像、身份类型等非敏感信息")
    public Result updateUserInfo(
            @Parameter(description = "用户信息（含更新字段）", required = true)
            @RequestBody User user,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);

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

    @GetMapping("/addresses")
    @Operation(summary = "获取用户地址列表", description = "查询当前用户的所有收货地址，包含默认地址标识")
    public Result getUserAddresses(
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        List<Address> addresses = addressService.getUserAddresses(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("addresses", addresses);
        return Result.success(result);
    }

    @PostMapping("/addresses")
    @Operation(summary = "添加新地址", description = "新增用户收货地址，若为首次添加则自动设为默认地址")
    public Result addAddress(
            @Parameter(description = "地址信息（含收货人、电话、详细地址等）", required = true)
            @RequestBody Address address,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return addressService.addAddress(address, userId);
    }

    @PutMapping("/addresses")
    @Operation(summary = "更新地址", description = "修改已有地址信息，若设为默认则自动取消其他地址的默认标识")
    public Result updateAddress(
            @Parameter(description = "更新后的地址信息（需包含地址ID）", required = true)
            @RequestBody Address address,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return addressService.updateAddress(address, userId);
    }

    @DeleteMapping("/addresses/{id}")
    @Operation(summary = "删除地址", description = "删除指定地址，若删除默认地址则自动将首个地址设为默认")
    public Result deleteAddress(
            @Parameter(description = "地址ID", required = true, example = "2001")
            @PathVariable Long id,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return addressService.deleteAddress(id, userId);
    }

    @PostMapping("/addresses/{id}/default")
    @Operation(summary = "设置默认地址", description = "将指定地址设为默认，自动取消其他地址的默认标识")
    public Result setDefaultAddress(
            @Parameter(description = "地址ID", required = true, example = "2001")
            @PathVariable Long id,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        return addressService.setDefaultAddress(id, userId);
    }
}