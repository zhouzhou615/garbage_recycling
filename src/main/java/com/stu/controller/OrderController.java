package com.stu.controller;

import com.stu.service.OrderService;
import com.stu.util.JwtUtil;
import com.stu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    // 创建个人回收订单
    @PostMapping("/personal")
    public Result createPersonalOrder(@RequestBody Map<String, Object> orderData,
                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long userId = jwtUtil.getUserIdFromToken(token);
        return orderService.createPersonalOrder(orderData, userId);
    }

    // 获取用户订单列表
    @GetMapping("/list")
    public Result getUserOrders(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestHeader("Authorization") String authHeader) {
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long userId = jwtUtil.getUserIdFromToken(token);
        return orderService.getUserOrders(userId, page, size);
    }

    // 取消订单（示例，需根据实际参数完善）
    @PostMapping("/cancel/{orderId}")
    public Result cancelOrder(@PathVariable Long orderId,
                              @RequestHeader("Authorization") String authHeader) {
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long userId = jwtUtil.getUserIdFromToken(token);
        return orderService.cancelOrder(orderId, userId);
    }
}

