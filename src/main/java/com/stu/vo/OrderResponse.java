package com.stu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "企业订单响应模型")
public class OrderResponse {
    private Long id;
    private String orderNo;
    private String orderType;
    private BigDecimal estimatedAmount;
    private String status;

    public static OrderResponse fromOrder(com.stu.entity.EnterpriseOrder order) {
        OrderResponse r = new OrderResponse();
        r.setId(order.getId());
        r.setOrderNo(order.getOrderNo());
        r.setOrderType(order.getOrderType());
        r.setEstimatedAmount(order.getEstimatedAmount());
        r.setStatus(order.getStatus());
        return r;
    }
}
