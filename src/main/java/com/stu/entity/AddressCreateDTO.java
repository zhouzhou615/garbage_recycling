package com.stu.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 地址创建请求 DTO
 */
@Data
public class AddressCreateDTO {
    @NotBlank(message = "地址标签不能为空")
    private String addressLabel; // 地址标签（家/学校/公司）

    @NotBlank(message = "详细地址不能为空")
    private String detailAddress; // 详细地址

    @NotBlank(message = "联系人不能为空")
    private String contactName; // 联系人姓名

    @NotBlank(message = "联系电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String contactPhone; // 联系电话

    private Double lat; // 纬度
    private Double lng; // 经度

    private Integer isDefault = 0; // 是否默认地址 1是 0否
}

