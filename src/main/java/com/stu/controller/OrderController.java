package com.stu.controller;

import com.stu.service.OrderService;
import com.stu.util.JwtUtil;
import com.stu.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/order")
@Tag(name = "订单管理接口", description = "个人回收订单的创建、查询、取消等操作")
@SecurityRequirement(name = "bearerAuth") // 需JWT认证
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/personal")
    @Operation(summary = "创建个人回收订单", description = "用户提交回收订单信息，包括地址、物品类型、预约时间等")
    public Result createPersonalOrder(
            @Parameter(description = "订单数据，包含addressId(地址ID)、items(物品列表)、appointmentTime(预约时间)等", required = true)
            @RequestBody Map<String, Object> orderData,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long userId = jwtUtil.getUserIdFromToken(token);
        return orderService.createPersonalOrder(orderData, userId);
    }

    @GetMapping("/list")
    @Operation(summary = "获取用户订单列表", description = "分页查询当前用户的所有回收订单，按创建时间倒序排列")
    public Result getUserOrders(
            @Parameter(description = "页码，默认1", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页条数，默认10", example = "10")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long userId = jwtUtil.getUserIdFromToken(token);
        return orderService.getUserOrders(userId, page, size);
    }

    @PostMapping("/cancel/{orderId}")
    @Operation(summary = "取消订单", description = "仅允许取消状态为'待接单'的订单")
    public Result cancelOrder(
            @Parameter(description = "订单ID", required = true, example = "1001")
            @PathVariable Long orderId,
            @Parameter(description = "请求头中的Authorization，格式为Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader != null && authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        Long userId = jwtUtil.getUserIdFromToken(token);
        return orderService.cancelOrder(orderId, userId);
    }
}