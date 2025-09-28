package com.stu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`order`") // 使用反引号因为order是SQL关键字
@Schema(description = "订单实体类，存储垃圾回收订单的核心信息")
public class Order {

    @TableId(type = IdType.AUTO)
    @Schema(description = "订单ID（自增主键）", example = "10001")
    private Long id;

    @Schema(description = "订单编号（唯一标识，格式如ORD20240927001）", example = "ORD20240927001")
    private String orderNo;

    @Schema(description = "下单用户ID（关联user表）", example = "20001")
    private Long userId;

    @Schema(description = "收货地址ID（关联address表）", example = "30001")
    private Long addressId;

    @Schema(description = "订单类型（1-个人订单，2-企业订单）", example = "1")
    private Integer orderType;

    @Schema(description = "订单状态（0-待接单，1-已接单，2-回收中，3-已完成，4-已取消）", example = "0")
    private Integer status;

    @Schema(description = "回收物品信息（JSON字符串，格式如[{\"categoryId\":1,\"name\":\"废纸\",\"weight\":2.5}]）",
            example = "[{\"categoryId\":1,\"name\":\"废纸\",\"weight\":2.5},{\"categoryId\":3,\"name\":\"塑料瓶\",\"weight\":1.2}]")
    private String items = "[]"; // JSON字符串存储物品信息

    @Schema(description = "预约回收时间", example = "2024-09-30 15:30:00")
    private Date scheduledTime;
    // 在Order.java中添加高校订单特有字段
    @TableField("campus_type")
    private Integer campusType; // 高校订单子类型: 1-旧教材回收 2-宿舍批量回收

    @Schema(description = "物品图片URL集合（JSON字符串，存储回收物品的现场照片）",
            example = "[\"https://example.com/imgs/order1001-1.jpg\",\"https://example.com/imgs/order1001-2.jpg\"]")
    private String images; // JSON字符串存储图片URL

    @Schema(description = "回收员ID（关联回收员表，未接单时为null）", example = "40001")
    private Long collectorId;

    @Schema(description = "订单扩展信息（JSON字符串，存储额外业务数据如备注等）",
            example = "{\"remark\":\"请放在小区门口保安室\",\"isUrgent\":false}")
    private String extendedOrderInfo; // JSON字符串存储扩展信息

    @Schema(description = "预估金额（元，根据物品类型和重量计算）", example = "15.60")
    private BigDecimal estimatedAmount; // 预估金额

    @Schema(description = "最终金额（元，回收完成后确认的实际金额）", example = "16.20")
    private BigDecimal finalAmount; // 最终金额
    @TableField("campus_info")
    private String campusInfo; // 高校订单特有信息(JSON)

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "订单创建时间", example = "2024-09-27 10:15:30")
    private Date createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "订单更新时间（状态变更或信息修改时自动更新）", example = "2024-09-27 10:15:30")
    private Date updatedAt;
}
