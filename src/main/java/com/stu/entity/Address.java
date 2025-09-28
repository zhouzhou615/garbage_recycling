package com.stu.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("address")
@Schema(description = "用户地址实体类，存储用户的收货地址信息（支持多地址管理和默认地址设置）")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "地址ID（自增主键）", example = "2001")
    private Long id;

    @TableField("user_id")
    @Schema(description = "关联用户ID（所属用户）", example = "1001")
    private Long userId;

    @TableField("receiver")
    @Schema(description = "收货人姓名", example = "张三")
    private String receiver;

    @TableField("phone")
    @Schema(description = "收货人联系电话", example = "13800138000")
    private String phone;

    @TableField("province")
    @Schema(description = "省份（如：广东省）", example = "广东省")
    private String province;

    @TableField("city")
    @Schema(description = "城市（如：深圳市）", example = "深圳市")
    private String city;

    @TableField("district")
    @Schema(description = "区/县（如：南山区）", example = "南山区")
    private String district;

    @TableField("detail_address")
    @Schema(description = "详细地址（街道、门牌号等）", example = "科技园路100号腾讯大厦A座15层")
    private String detailAddress;

    @TableField("is_default")
    @Schema(description = "是否默认地址（1-默认地址，0-非默认地址）", example = "1")
    private Integer isDefault; // 1-默认地址 0-非默认

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Schema(description = "地址创建时间", example = "2024-09-05 11:20:30")
    private Date createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "地址更新时间（修改时自动更新）", example = "2024-09-10 16:45:12")
    private Date updatedAt;

    @TableField("deleted")
    @TableLogic
    @Schema(description = "逻辑删除标识（0-未删除，1-已删除）", example = "0", hidden = true)
    private Integer deleted;
}
    