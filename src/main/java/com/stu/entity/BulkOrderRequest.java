package com.stu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "企业批量回收下单请求参数")
public class BulkOrderRequest {

    @NotNull
    @Schema(description = "物品明细列表")
    private List<OrderItem> items;

    @NotNull
    @DecimalMin(value = "0.1", message = "重量必须大于0")
    @Schema(description = "总重量")
    private BigDecimal totalWeight;

    @Schema(description = "重量单位，默认KG")
    private WeightUnit weightUnit = WeightUnit.KG;

    @Schema(description = "是否需要发票，默认false")
    private Boolean invoiceRequired = false;

    @NotBlank
    @Schema(description = "上门回收地址")
    private String pickupAddress;

    @NotBlank
    @Schema(description = "联系人姓名")
    private String contactPerson;

    @NotBlank
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "联系电话（中国大陆手机号）")
    private String contactPhone;

    @Future(message = "预约时间需晚于当前时间")
    @Schema(description = "预约/计划执行时间，可选")
    private LocalDateTime scheduleTime;

    @Schema(description = "发票信息（需要发票时填写）")
    private InvoiceInfo invoiceInfo;
}

