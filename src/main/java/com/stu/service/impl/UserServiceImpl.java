package com.stu.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stu.entity.User;
import com.stu.mapper.UserMapper;
import com.stu.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.stu.vo.UserUpdateVO;
import java.util.Date;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    // 注入JSON工具类，用于校验extended_info是否为合法JSON格式
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public User getByOpenid(String openid) {
        return baseMapper.selectByOpenid(openid);
    }

    @Override
    @Transactional
    public User createOrUpdateUser(User user) {
        // 1. 处理extended_info字段：确保为有效JSON，避免"undefined"等非法值
        String validExtendedInfo = handleValidExtendedInfo(user.getExtendedInfo());
        user.setExtendedInfo(validExtendedInfo); // 覆盖为合法值

        User existingUser = getByOpenid(user.getOpenid());

        if (existingUser != null) {
            // 2. 用户已存在：执行更新操作（使用处理后的合法extended_info）
            LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(User::getId, existingUser.getId())
                    .set(User::getNickname, user.getNickname())
                    .set(User::getAvatarUrl, user.getAvatarUrl())
                    .set(User::getLastLoginAt, new Date()) // 每次登录更新最后登录时间
                    .set(User::getUpdatedAt, new Date()); // 更新用户信息时同步更新修改时间

            // 条件更新：仅当传入非null值时才更新对应字段
            if (user.getIdentityType() != null) {
                updateWrapper.set(User::getIdentityType, user.getIdentityType());
            }
            if (user.getSchoolName() != null) {
                updateWrapper.set(User::getSchoolName, user.getSchoolName());
            }
            if (user.getDepartment() != null) {
                updateWrapper.set(User::getDepartment, user.getDepartment());
            }
            // 无需判断null：handleValidExtendedInfo已确保extendedInfo为合法值（至少是"{}"）
            updateWrapper.set(User::getExtendedInfo, user.getExtendedInfo());

            baseMapper.update(null, updateWrapper);
            return getByOpenid(user.getOpenid()); // 返回更新后的最新用户信息
        } else {
            // 3. 用户不存在：执行创建操作（使用处理后的合法extended_info）
            user.setCreatedAt(new Date());
            user.setUpdatedAt(new Date());
            user.setLastLoginAt(new Date());
            user.setDeleted(0); // 0表示未删除（软删除逻辑）
            baseMapper.insert(user);
            return user;
        }
    }
    @Override
    @Transactional
    public User completeUserInfo(Long userId, UserUpdateVO userUpdateVO) {
        // 1. 校验用户是否存在
        User existingUser = baseMapper.selectById(userId);
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 处理extended_info字段
        String validExtendedInfo = handleValidExtendedInfo(userUpdateVO.getExtendedInfo());

        // 3. 构建更新条件
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, userId)
                .set(User::getNickname, userUpdateVO.getNickname())        // 更新昵称
                .set(User::getAvatarUrl, userUpdateVO.getAvatarUrl())      // 更新头像
                .set(User::getIdentityType, userUpdateVO.getIdentityType())
                .set(User::getSchoolName, userUpdateVO.getSchoolName())
                .set(User::getDepartment, userUpdateVO.getDepartment())
                .set(User::getExtendedInfo, validExtendedInfo)
                .set(User::getUpdatedAt, new Date());

        // 4. 执行更新
        baseMapper.update(null, updateWrapper);

        // 5. 返回更新后的用户信息
        return baseMapper.selectById(userId);
    }
    @Override
    public User getUserInfoById(Long userId) {
        User user = baseMapper.selectById(userId);
        if (user != null) {
            // 可以在这里过滤敏感字段，或者创建新的DTO返回
            user.setDeleted(null); // 排除逻辑删除字段
        }
        return user;
    }

    /**
     * 处理extended_info字段，确保其为有效JSON格式
     * @param originalExtendedInfo 原始传入的extended_info值（可能为null、"undefined"、非法JSON等）
     * @return 合法的JSON字符串（空JSON对象"{}"或有效JSON）
     */
    private String handleValidExtendedInfo(String originalExtendedInfo) {
        // 默认返回空JSON对象（符合数据库JSON类型要求，且贴合业务中"差异信息可选"的设计）
        String defaultValidJson = "{}";

        // 情况1：原始值为null或空字符串，直接返回默认空JSON
        if (originalExtendedInfo == null || originalExtendedInfo.trim().isEmpty()) {
            logger.debug("extended_info为null或空，使用默认空JSON");
            return defaultValidJson;
        }

        String trimmedValue = originalExtendedInfo.trim();

        // 情况2：原始值为"undefined"或"null"（前端常见非法值），返回默认空JSON
        if ("undefined".equals(trimmedValue) || "null".equals(trimmedValue)) {
            logger.warn("extended_info为非法值[{}]，已转为空JSON", trimmedValue);
            return defaultValidJson;
        }

        // 情况3：校验是否为合法JSON格式
        try {
            // 若能成功解析为JSON树结构，说明是合法JSON
            objectMapper.readTree(trimmedValue);
            logger.debug("extended_info为合法JSON，直接使用");
            return trimmedValue;
        } catch (Exception e) {
            // 解析失败，说明是非法JSON，返回默认空JSON并记录日志
            logger.error("extended_info格式非法[{}]，已转为空JSON，错误原因：{}", trimmedValue, e.getMessage());
            return defaultValidJson;
        }
    }
}