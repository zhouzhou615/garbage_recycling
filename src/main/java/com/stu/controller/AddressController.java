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

// 新增：Swagger 注解导入
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/address")
@Tag(name = "地址管理", description = "用户收货地址的新增、列表与默认地址设置")
@SecurityRequirement(name = "bearerAuth") // 需要JWT认证
public class AddressController {

    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 创建地址
     */
    @PostMapping("/create")
    @Operation(summary = "创建地址", description = "新增用户收货地址，支持设置为默认地址")
    public Result create(
            @Parameter(description = "地址创建参数", required = true)
            @RequestBody @Valid AddressCreateDTO dto,
            @Parameter(description = "请求头中的授权信息，格式为 Bearer {token}", required = true)
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
    @Operation(summary = "地址列表", description = "查询当前登录用户的所有收货地址")
    public Result list(
            @Parameter(description = "请求头中的授权信息，格式为 Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserId(authHeader);
        List<UserAddress> list = userAddressService.listByUserId(userId);
        return Result.success().put("addresses", list);
    }

    /**
     * 设置默认地址
     */
    @PostMapping("/default/{id}")
    @Operation(summary = "设置默认地址", description = "将指定地址设为默认地址，并取消其他地址的默认标记")
    public Result setDefault(
            @Parameter(description = "地址ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "请求头中的授权信息，格式为 Bearer {token}", required = true)
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
