package com.stu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.stu.entity.Order;
import com.stu.vo.Result;

import java.util.Map;

public interface PersonOrderService extends IService<Order> {
    Result createPersonalOrder(Map<String, Object> orderData, Long userId);
    Result getUserOrders(Long userId, Integer page, Integer size);
    Result cancelOrder(Long orderId, Long userId);
}