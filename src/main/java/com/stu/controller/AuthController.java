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
                             @RequestParam(required = false) String avatarUrl,
                             @RequestParam(required = false, defaultValue = "1") Integer identityType,
                             @RequestParam(required = false) String schoolName,
                             @RequestParam(required = false) String department,
                             @RequestParam(required = false) String extendedInfo) {
        try {
            logger.info("微信登录请求: code={}, identityType={}", code, identityType);

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
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("message", "Token有效");
            resultData.put("user", user);
            return Result.success(resultData);

        } else {
            return Result.error("Token无效或已过期");
        }
    }
}