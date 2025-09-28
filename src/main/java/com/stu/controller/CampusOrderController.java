package com.stu.controller;


import com.stu.entity.DormitoryOrderDTO;
import com.stu.entity.TextbookOrderDTO;
import com.stu.service.CampusOrderService;
import com.stu.util.JwtUtil;
import com.stu.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campusOrder")
public class CampusOrderController {
    @Autowired
    private CampusOrderService campusOrderService;
    @Autowired
    private JwtUtil jwtUtil;
    // 创建教材回收订单
    @PostMapping("/textbook")
    public Result createTextbookOrder(@RequestBody @jakarta.validation.Valid TextbookOrderDTO orderDTO,
                                      @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromHeader(authHeader);
        return campusOrderService.createTextbookOrder(orderDTO, userId);
    }
    // 创建宿舍回收订单
    @PostMapping("/dormitory")
    public Result createDormitoryOrder(@RequestBody @jakarta.validation.Valid DormitoryOrderDTO orderDTO,
                                       @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromHeader(authHeader);
        return campusOrderService.createDormitoryOrder(orderDTO, userId);
    }
    // 从请求头中提取用户ID
    private Long getUserIdFromHeader(String authHeader) {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;
        return jwtUtil.getUserIdFromToken(token);
    }
}
