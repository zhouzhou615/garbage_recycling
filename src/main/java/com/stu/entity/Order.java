package com.stu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`order`") // 使用反引号因为order是SQL关键字
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id; // 主键ID
    private String orderNo; // 订单号（唯一）
    private Long userId; // 下单用户ID
    private Long addressId; // 地址ID（用户选择的回收地址）
    private Integer orderType; // 订单类型：1-个人回收 2-高校回收
    private Integer status; // 订单状态：1-待上门 0-已取消 等
    private String items = "[]"; // JSON字符串存储物品信息; 默认空数组，防止 NOT NULL 约束报错
    private Date scheduledTime; // 预约上门时间
    private String images; // 上传的图片(数组JSON字符串)
    private Long collectorId; // 分配的回收人员ID（可为空）
    private String extendedOrderInfo; // 扩展信息(JSON)
    private BigDecimal estimatedAmount; // 预估金额（高校订单/需要估价的场景）
    private BigDecimal finalAmount; // 最终成交金额

    // 在Order.java中添加高校订单特有字段
    @TableField("campus_type")
    private Integer campusType; // 高校订单子类型: 1-旧教材回收 2-宿舍批量回收

    @TableField("campus_info")
    private String campusInfo; // 高校订单特有信息(JSON)

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt; // 更新时间
}