package com.stu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("`order`") // 使用反引号因为order是SQL关键字
public class Order {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private Long userId;
    private Long addressId;
    private Integer orderType;
    private Integer status;
    private String items; // JSON字符串存储物品信息
    private Date scheduledTime;
    private String images; // JSON字符串存储图片URL
    private Long collectorId;
    private String extendedOrderInfo; // JSON字符串存储扩展信息
    private BigDecimal estimatedAmount; // 预估金额
    private BigDecimal finalAmount; // 最终金额

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;
}