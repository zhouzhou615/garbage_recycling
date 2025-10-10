package com.stu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("enterprise_orders")
@Schema(description = "企业订单实体：企业批量/定期回收订单")
public class EnterpriseOrder implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键ID")
    private Long id;

    @TableField("order_no")
    @Schema(description = "订单编号")
    private String orderNo;

    @TableField("enterprise_id")
    @Schema(description = "企业用户ID")
    private Long enterpriseId;

    @TableField("order_type")
    @Schema(description = "订单类型: BULK批量/REGULAR定期")
    private String orderType;

    @TableField("total_weight")
    @Schema(description = "总重量")
    private BigDecimal totalWeight;

    @TableField("weight_unit")
    @Schema(description = "重量单位: KG/TON")
    private String weightUnit;

    @TableField("estimated_amount")
    @Schema(description = "预估金额")
    private BigDecimal estimatedAmount;

    @TableField("actual_amount")
    @Schema(description = "实际金额")
    private BigDecimal actualAmount;

    @TableField("invoice_required")
    @Schema(description = "是否需要发票")
    private Boolean invoiceRequired;

    @TableField("invoice_title")
    @Schema(description = "发票抬头")
    private String invoiceTitle;

    @TableField("tax_number")
    @Schema(description = "税号")
    private String taxNumber;

    @TableField("invoice_status")
    @Schema(description = "发票状态")
    private String invoiceStatus;

    @TableField("plan_id")
    @Schema(description = "关联的回收计划ID")
    private Long planId;

    @TableField("schedule_time")
    @Schema(description = "计划执行时间")
    private Date scheduleTime;

    @TableField("status")
    @Schema(description = "订单状态")
    private String status;

    @TableField("pickup_address")
    @Schema(description = "上门地址")
    private String pickupAddress;

    @TableField("contact_person")
    @Schema(description = "联系人")
    private String contactPerson;

    @TableField("contact_phone")
    @Schema(description = "联系电话")
    private String contactPhone;

    @TableField("created_at")
    @Schema(description = "创建时间")
    private Date createdAt;

    @TableField("updated_at")
    @Schema(description = "更新时间")
    private Date updatedAt;
}
