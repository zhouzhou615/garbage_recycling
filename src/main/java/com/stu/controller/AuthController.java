package com.stu.controller;

import com.stu.entity.User;
import com.stu.service.UserService;
import com.stu.util.JwtUtil;
import com.stu.util.WeChatUtil;
import com.stu.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.stu.vo.UserUpdateVO;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private WeChatUtil weChatUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/wechat/login")
    public Result wechatLogin(@RequestParam String code,
                              @RequestParam(required = false) String nickname,
                              @RequestParam(required = false) String avatarUrl) {
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
            user.setIdentityType(1); // 默认个人用户

            User savedUser = userService.createOrUpdateUser(user);
            logger.info("用户登录成功: userId={}, openid={}", savedUser.getId(), savedUser.getOpenid());

            // 3. 生成JWT Token
            String token = jwtUtil.generateToken(savedUser.getId(), openid);

            // 4. 构建返回的用户信息（排除敏感字段）
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", savedUser.getId());
            userInfo.put("openid", savedUser.getOpenid());
            userInfo.put("nickname", savedUser.getNickname());
            userInfo.put("avatarUrl", savedUser.getAvatarUrl());
            userInfo.put("identityType", savedUser.getIdentityType());
            userInfo.put("schoolName", savedUser.getSchoolName());
            userInfo.put("department", savedUser.getDepartment());

            // 5. 返回结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "登录成功");
            resultData.put("token", token);
            resultData.put("user", userInfo);

            return Result.success(resultData);

        } catch (Exception e) {
            logger.error("微信登录异常: {}", e.getMessage(), e);
            return Result.error("微信登录失败: " + e.getMessage());
        }
    }

    @PutMapping("/completeUserInfo")
    public Result completeUserInfo(@RequestBody UserUpdateVO userUpdateVO,
                                   @RequestHeader("Authorization") String authHeader) {
        try {
            logger.info("完善用户信息请求: identityType={}", userUpdateVO.getIdentityType());

            // 1. 验证Token并获取用户ID
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Result.error("未提供有效的Token");
            }

            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return Result.error("Token无效或已过期");
            }

            Long userId = jwtUtil.getUserIdFromToken(token);

            // 2. 完善用户信息
            User updatedUser = userService.completeUserInfo(userId, userUpdateVO);
            logger.info("用户信息完善成功: userId={}", userId);

            // 3. 构建返回的用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", updatedUser.getId());
            userInfo.put("nickname", updatedUser.getNickname());
            userInfo.put("avatarUrl", updatedUser.getAvatarUrl());
            userInfo.put("identityType", updatedUser.getIdentityType());
            userInfo.put("schoolName", updatedUser.getSchoolName());
            userInfo.put("department", updatedUser.getDepartment());

            // 4. 返回结果
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "用户信息完善成功");
            resultData.put("user", userInfo);
            return Result.success(resultData);

        } catch (Exception e) {
            logger.error("完善用户信息异常: {}", e.getMessage(), e);
            return Result.error("完善用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息（排除敏感信息）
     */
    @GetMapping("/currentUser")
    public Result getCurrentUser(@RequestHeader("Authorization") String authHeader) {
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
    public Result checkToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
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
            resultData.put("user", userInfo);
            return Result.success(resultData);

        } else {
            return Result.error("Token无效或已过期");
        }
    }
}