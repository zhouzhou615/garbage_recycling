package com.stu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stu.entity.Order;
import com.stu.mapper.OrderMapper;
import com.stu.service.OrderService;
import com.stu.util.JwtUtil;
import com.stu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;



import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional
    public Result createPersonalOrder(Map<String, Object> orderData, Long userId) {
        try {
            Order order = new Order();
            ObjectMapper objectMapper = new ObjectMapper();
            // 生成唯一订单号
            String orderNo = "ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
            order.setOrderNo(orderNo);
            order.setUserId(userId);
            order.setOrderType(1); // 1表示个人回收订单
            order.setStatus(1); // 1表示待上门

            // 设置地址ID
            if (orderData.containsKey("addressId")) {
                order.setAddressId(Long.valueOf(orderData.get("addressId").toString()));
            }

            // 设置预约时间
            if (orderData.containsKey("scheduledTime")) {
                String timeStr = orderData.get("scheduledTime").toString();
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date scheduledTime = sdf.parse(timeStr);
                    order.setScheduledTime(scheduledTime);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return Result.error("预约时间格式错误");
                }
            }

            // 设置物品信息（JSON格式）
            if (orderData.containsKey("items")) {
                String itemsJson = objectMapper.writeValueAsString(orderData.get("items"));
                order.setItems(itemsJson);
                order.setEstimatedAmount(calculateEstimatedAmount(itemsJson));
            }

            // 设置图片信息（JSON格式）
            if (orderData.containsKey("images")) {
                String imagesJson = objectMapper.writeValueAsString(orderData.get("images"));
                order.setImages(imagesJson);
            }

            order.setCreatedAt(new Date());
            order.setUpdatedAt(new Date());

            // 保存订单
            int result = baseMapper.insert(order);
            System.out.println("插入结果: " + result + ", 订单ID: " + order.getId());

            Map<String, Object> resultData = new HashMap<>();
            resultData.put("orderNo", orderNo);
            resultData.put("orderId", order.getId());
            return Result.success(resultData);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("创建订单失败: " + e.getMessage());
        }
    }

    // 计算预估金额的方法（需要根据实际业务逻辑实现）
    private BigDecimal calculateEstimatedAmount(String itemsJson) {
        // 这里应该解析itemsJson并计算总金额
        // 示例：返回一个固定值
        return new BigDecimal("25.50");
    }
    // 实现获取用户订单列表的逻辑
    // 可以使用MyBatis-Plus的分页功能
    public Result getUserOrders(Long userId, Integer page, Integer size) {
        Page<Order> orderPage = new Page<>(page, size);
        QueryWrapper<Order> query = new QueryWrapper<>();
        query.eq("user_id", userId);
        orderPage = baseMapper.selectPage(orderPage, query);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("orders", orderPage.getRecords());
        resultData.put("total", orderPage.getTotal());
        return Result.success(resultData);
    }


    // 实现取消订单的逻辑
    // 需要检查订单是否属于该用户，以及订单状态是否允许取消
    @Override
    @Transactional
    public Result cancelOrder(Long orderId, Long userId) {
        Order order = baseMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return Result.error("订单不存在或不属于该用户");
        }
        if (order.getStatus() != 1) { // 1表示待上门
            return Result.error("订单当前状态不允许取消");
        }
        order.setStatus(0); // 0表示已取消
        order.setUpdatedAt(new Date());
        baseMapper.updateById(order);

        Map<String, Object> resultData = new HashMap<>();
        resultData.put("message", "订单取消成功");
        return Result.success(resultData);
    }
}