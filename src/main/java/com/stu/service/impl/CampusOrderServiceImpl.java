package com.stu.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.stu.entity.DormitoryOrderDTO;
import com.stu.entity.Order;
import com.stu.entity.TextbookOrderDTO;
import com.stu.entity.User;
import com.stu.mapper.OrderMapper;
import com.stu.service.CampusOrderService;
import com.stu.service.UserService;
import com.stu.service.UserAddressService;
import com.stu.vo.Result;
import com.stu.util.SnowflakeIdGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import  com.stu.service.PriceCalculationService;

@Service
public class CampusOrderServiceImpl implements CampusOrderService {

    public static final int CAMPUS_USER = 2;
    @Autowired
    private SnowflakeIdGenerator idGenerator;// 用于生成唯一订单号

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private  PriceCalculationService priceCalculationService;
    @Autowired
    private UserAddressService userAddressService; // 新增：地址归属校验服务

    @Override
    @Transactional
    public Result createTextbookOrder(TextbookOrderDTO orderDTO, Long userId) {
        if (!validateCampusUser(userId)) {
            return Result.error("只有高校用户可以下单");
        }
        // 地址校验
        if (orderDTO.getAddressId() == null || !userAddressService.validateUserAddress(orderDTO.getAddressId(), userId)) {
            return Result.error("地址无效或不属于当前用户");
        }
        Order order = new Order();// 新建订单对象
        order.setOrderNo(generateOrderNo());// 生成唯一订单号
        order.setUserId(userId);// 设置用户ID
        order.setAddressId(orderDTO.getAddressId());// 设置地址ID
        order.setOrderType(2);// 订单类型为高校订单
        order.setCampusType(1);// 校园订单类型为旧教材回收
        order.setStatus(1);// 设置订单初始状态为待处理

        Map<String, Object> campusInfo = new HashMap<>();// 创建存储高校订单特有信息的Map
        campusInfo.put("textbookType", orderDTO.getTextbookType());// 教材类型
        campusInfo.put("quantity", orderDTO.getQuantity());// 教材数量
        campusInfo.put("unit", orderDTO.getUnit());// 教材单位
        campusInfo.put("condition", orderDTO.getCondition());// 教材新旧程度
        campusInfo.put("pickupLocation", orderDTO.getPickupLocation());// 取件地点
        campusInfo.put("schoolName", orderDTO.getSchoolName());// 学校名称
        campusInfo.put("department", orderDTO.getDepartment());// 系别
        try {
            order.setCampusInfo(objectMapper.writeValueAsString(campusInfo));
            order.setScheduledTime(orderDTO.getScheduledTime());
            if (orderDTO.getImages() != null) {
                order.setImages(objectMapper.writeValueAsString(orderDTO.getImages()));
            }
            order.setEstimatedAmount(calculateTextbookAmount(orderDTO));
            orderMapper.insert(order);
            return Result.success().put("orderNo", order.getOrderNo());
        } catch (Exception e) {
            throw new RuntimeException("创建教材回收订单失败", e);
        }
    }

    @Override
    @Transactional
    public Result createDormitoryOrder(DormitoryOrderDTO orderDTO, Long userId) {
        if (!validateCampusUser(userId)) {
            return Result.error("只有高校用户可以下单");
        }
        // 地址校验
        if (orderDTO.getAddressId() == null || !userAddressService.validateUserAddress(orderDTO.getAddressId(), userId)) {
            return Result.error("地址无效或不属于当前用户");
        }
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setAddressId(orderDTO.getAddressId());
        order.setOrderType(2);
        order.setCampusType(2);
        order.setStatus(1);

        Map<String, Object> campusInfo = new HashMap<>();
        campusInfo.put("dormitoryBuilding", orderDTO.getDormitoryBuilding());
        campusInfo.put("roomRange", orderDTO.getRoomRange());
        campusInfo.put("recyclingCategories", orderDTO.getRecyclingCategories());
        campusInfo.put("contactPerson", orderDTO.getContactPerson());
        campusInfo.put("contactPhone", orderDTO.getContactPhone());
        campusInfo.put("schoolName", orderDTO.getSchoolName());
        campusInfo.put("department", orderDTO.getDepartment());
        try {
            order.setCampusInfo(objectMapper.writeValueAsString(campusInfo));
            order.setScheduledTime(orderDTO.getScheduledTime());
            if (orderDTO.getImages() != null) {
                order.setImages(objectMapper.writeValueAsString(orderDTO.getImages()));
            }
            order.setEstimatedAmount(calculateDormitoryAmount(orderDTO));
            orderMapper.insert(order);
            return Result.success().put("orderNo", order.getOrderNo());
        } catch (Exception e) {
            throw new RuntimeException("创建宿舍批量回收订单失败", e);
        }
    }

    @Override
    public boolean validateCampusUser(Long userId) {
        User user = userService.getById(userId);
        return user != null && user.getIdentityType() == CAMPUS_USER;
    }

    private String generateOrderNo() {
         return "CAMP" + idGenerator.nextId();
    }
    // 估算教材回收订单金额的简单示例
    private BigDecimal calculateTextbookAmount(TextbookOrderDTO textbookOrderDTO) {
        return priceCalculationService.calculateTextbookAmount(textbookOrderDTO);
    }
    private BigDecimal calculateDormitoryAmount(DormitoryOrderDTO dormitoryOrderDTO) {
        return priceCalculationService.calculateDormitoryAmount(dormitoryOrderDTO);
    }
}
