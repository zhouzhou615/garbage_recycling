package com.stu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

/**
 * 用户地址实体 (与现有表结构保持一致)
 * 表结构字段:
 * id, user_id, address_label, detail_address, contact_name, contact_phone, location(JSON), is_default, created_at, updated_at
 */
@Data
@TableName("user_address")
public class UserAddress {
    @TableId(type = IdType.AUTO)
    private Long id;                 // 主键ID
    private Long userId;             // 用户ID
    private String addressLabel;     // 地址标签：家/学校/公司
    private String detailAddress;    // 详细地址
    private String contactName;      // 联系人姓名
    private String contactPhone;     // 联系电话
    private String location;         // 地理位置(JSON：经纬度等)，表为 JSON 字段，这里用 String 承接
    private Integer isDefault;       // 是否默认地址：1-是 0-否

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;          // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;          // 更新时间

    // 说明：旧版本设计的 province/city/district 等字段在当前表中不存在，因此不再定义。
    // 如果后续需要扩展，应先在数据库中增加列，再在此处添加字段。
}
