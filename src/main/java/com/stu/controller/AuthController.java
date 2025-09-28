package com.stu.controller;


import com.stu.entity.User;
import com.stu.service.UserService;
import com.stu.util.JwtUtil;
import com.stu.util.WeChatUtil;
import com.stu.vo.Result;
import com.stu.vo.UserUpdateVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "认证接口", description = "微信登录、Token验证、用户信息管理")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private WeChatUtil weChatUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/wechat/login")
    @Operation(summary = "微信登录", description = "通过微信code获取openid并完成登录，返回JWT token")
    public Result wechatLogin(
            @Parameter(description = "微信登录临时code（通过wx.login获取）", required = true)
            @RequestParam String code,
            @Parameter(description = "用户昵称（可选）")
            @RequestParam(required = false) String nickname,
            @Parameter(description = "用户头像URL（可选）")
            @RequestParam(required = false) String avatarUrl,
            @RequestParam(required = false, defaultValue = "1") Integer identityType,
            @RequestParam(required = false) String schoolName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String extendedInfo) {
        try {
            logger.info("微信登录请求: code={}", code);

            // 1. 获取openid
            Map<String, String> wechatInfo = weChatUtil.getOpenidByCode(code);
            String openid = wechatInfo.get("openid");
            String unionid = wechatInfo.get("unionid");
            String errcode = wechatInfo.get("errcode");

            if (errcode != null && !errcode.equals("0")) {
                String errmsg = wechatInfo.get("errmsg");
                logger.error("微信登录失败: {} - {}", errcode, errmsg);
                return Result.error("微信登录失败: " + errmsg);
            }

            if (openid == null) {
                logger.error("微信登录失败: 无法获取openid");
                return Result.error("微信登录失败，无法获取用户信息");
            }

            // 2. 查询或创建用户
            User user = new User();
            user.setOpenid(openid);
            user.setUnionid(unionid);
            user.setNickname(nickname);
            user.setAvatarUrl(avatarUrl);
            user.setIdentityType(identityType);
            user.setSchoolName(schoolName);
            user.setDepartment(department);
            user.setExtendedInfo(extendedInfo);

            User savedUser = userService.createOrUpdateUser(user);
            logger.info("用户登录成功: userId={}, openid={}", savedUser.getId(), savedUser.getOpenid());

            // 3. 生成JWT Token
            String token = jwtUtil.generateToken(savedUser.getId(), openid);

            // 4. 返回结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "登录成功");
            resultData.put("token", token);
            resultData.put("user", savedUser);
            return Result.success(resultData);

        } catch (Exception e) {
            logger.error("微信登录异常: {}", e.getMessage(), e);
            return Result.error("微信登录失败: " + e.getMessage());
        }
    }

    @PutMapping("/completeUserInfo")
    @Operation(summary = "完善用户信息", description = "补充或修改用户身份类型、学校、院系等信息")
    @SecurityRequirement(name = "bearerAuth") // 需JWT认证
    public Result completeUserInfo(
            @Parameter(description = "用户信息更新数据", required = true)
            @RequestBody UserUpdateVO userUpdateVO,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        try {
            logger.info("完善用户信息请求: identityType={}", userUpdateVO.getIdentityType());

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的Token");
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return Result.error("Token无效或已过期");
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            User updatedUser = userService.completeUserInfo(userId, userUpdateVO);
            logger.info("用户信息完善成功: userId={}", userId);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", updatedUser.getId());
            userInfo.put("nickname", updatedUser.getNickname());
            userInfo.put("avatarUrl", updatedUser.getAvatarUrl());
            userInfo.put("identityType", updatedUser.getIdentityType());
            userInfo.put("schoolName", updatedUser.getSchoolName());
            userInfo.put("department", updatedUser.getDepartment());

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "用户信息完善成功");
            resultData.put("user", userInfo);
            return Result.success(resultData);

        } catch (Exception e) {
            logger.error("完善用户信息异常: {}", e.getMessage(), e);
            return Result.error("完善用户信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/currentUser")
    @Operation(summary = "获取当前用户信息", description = "通过Token获取登录用户的基本信息")
    @SecurityRequirement(name = "bearerAuth") // 需JWT认证
    public Result getCurrentUser(
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的Token");
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return Result.error("Token无效或已过期");
            }

            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getUserInfoById(userId);

            if (user == null) {
                return Result.error("用户不存在");
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("openid", user.getOpenid());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("avatarUrl", user.getAvatarUrl());
            userInfo.put("identityType", user.getIdentityType());
            userInfo.put("schoolName", user.getSchoolName());
            userInfo.put("department", user.getDepartment());

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("user", userInfo);
            return Result.success(resultData);

        } catch (Exception e) {
            logger.error("获取用户信息异常: {}", e.getMessage(), e);
            return Result.error("获取用户信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkToken")
    @Operation(summary = "验证Token有效性", description = "检查Token是否有效，返回用户信息（无需认证即可访问）")
    public Result checkToken(
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}")
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("未提供有效的Token");
        }

        String token = authHeader.substring(7);
        boolean isValid = jwtUtil.validateToken(token);

        if (isValid) {
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("openid", user.getOpenid());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("avatarUrl", user.getAvatarUrl());
            userInfo.put("identityType", user.getIdentityType());
            userInfo.put("schoolName", user.getSchoolName());
            userInfo.put("department", user.getDepartment());

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "Token有效");
            //resultData.put("user", user);
            resultData.put("user", userInfo);
            return Result.success(resultData);

        } else {
            return Result.error("Token无效或已过期");
        }
    }
}