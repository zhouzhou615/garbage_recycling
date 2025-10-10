package com.stu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("recycling_plans")
@Schema(description = "企业定期回收计划实体")
public class RecyclingPlan implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("plan_no")
    @Schema(description = "计划编号")
    private String planNo;

    @TableField("enterprise_id")
    @Schema(description = "企业ID")
    private Long enterpriseId;

    @TableField("plan_name")
    @Schema(description = "计划名称")
    private String planName;

    @TableField("cycle_type")
    @Schema(description = "回收周期: WEEKLY/BIWEEKLY/MONTHLY")
    private String cycleType;

    @TableField("total_cycles")
    @Schema(description = "总执行次数")
    private Integer totalCycles;

    @TableField("completed_cycles")
    @Schema(description = "已完成次数")
    private Integer completedCycles;

    @TableField("next_schedule_date")
    @Schema(description = "下次执行日期")
    private Date nextScheduleDate;

    @TableField("end_date")
    @Schema(description = "计划结束日期")
    private Date endDate;

    @TableField("items_config")
    @Schema(description = "回收物品配置（JSON）")
    private String itemsConfig;

    @TableField("invoice_config")
    @Schema(description = "发票配置（JSON）")
    private String invoiceConfig;

    @TableField("status")
    @Schema(description = "计划状态: ACTIVE/PAUSED/COMPLETED/CANCELLED")
    private String status;

    @TableField("created_at")
    @Schema(description = "创建时间")
    private Date createdAt;

    @TableField("updated_at")
    @Schema(description = "更新时间")
    private Date updatedAt;
}

