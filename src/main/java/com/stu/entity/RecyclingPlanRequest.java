package com.stu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "创建企业定期回收计划请求参数")
public class RecyclingPlanRequest {

    @NotBlank
    @Schema(description = "计划名称")
    private String planName;

    @NotBlank
    @Schema(description = "回收周期: WEEKLY/BIWEEKLY/MONTHLY")
    private String cycleType;

    @Positive
    @Schema(description = "总执行次数，可选，不填则无限期")
    private Integer totalCycles;

    @FutureOrPresent
    @Schema(description = "下次执行日期")
    private LocalDate nextScheduleDate;

    @Schema(description = "计划结束日期，可选")
    private LocalDate endDate;

    @NotBlank
    @Schema(description = "回收物品配置（JSON字符串）")
    private String itemsConfig;

    @Schema(description = "发票配置（JSON字符串）")
    private String invoiceConfig;

    @Schema(description = "是否立即开始，若为true将生成首单")
    private Boolean startImmediately = Boolean.FALSE;
}

