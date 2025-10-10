package com.stu.controller;


import com.stu.entity.DormitoryOrderDTO;
import com.stu.entity.TextbookOrderDTO;
import com.stu.service.CampusOrderService;
import com.stu.util.JwtUtil;
import com.stu.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campusOrder")
@Tag(name = "校园订单下单", description = "校园内教材/宿舍集中回收下单接口")
@SecurityRequirement(name = "bearerAuth") // 需要JWT认证
public class CampusOrderController {
    @Autowired
    private CampusOrderService campusOrderService;
    @Autowired
    private JwtUtil jwtUtil;

    // 创建教材回收订单
    @PostMapping("/textbook")
    @Operation(summary = "创建教材回收订单", description = "用于校园集中教材回收场景的下单接口，需传入教材信息、时间等参数")
    public Result createTextbookOrder(
            @Parameter(description = "教材回收订单参数", required = true)
            @RequestBody @jakarta.validation.Valid TextbookOrderDTO orderDTO,
            @Parameter(description = "请求头中的授权信息，格式为 Bearer {token}", required = true)
            @RequestHeader("Authorization") String authHeader) {
        Long userId = getUserIdFromHeader(authHeader);
        return campusOrderService.createTextbookOrder(orderDTO, userId);
    }

    // 创建宿舍回收订单
    @PostMapping("/dormitory")
    @Operation(summary = "创建宿舍回收订单", description = "用于宿舍统一回收场景的下单接口，需传入宿舍信息、回收时间段等参数")
    public Result createDormitoryOrder(
            @Parameter(description = "宿舍回收订单参数", required = true)
            @RequestBody @jakarta.validation.Valid DormitoryOrderDTO orderDTO,
            @Parameter(description = "请求头中的授权信息，格式为 Bearer {token}", required = true)
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
