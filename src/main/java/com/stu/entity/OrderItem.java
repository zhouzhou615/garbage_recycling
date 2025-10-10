package com.stu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "订单物品项")
public class OrderItem {
    @Schema(description = "物品类别编码，如 waste_paper/plastic_bottle")
    private String category;

    @Schema(description = "数量，可选")
    private Integer quantity;

    @Schema(description = "重量(公斤)，可选")
    private BigDecimal weightKg;
}

