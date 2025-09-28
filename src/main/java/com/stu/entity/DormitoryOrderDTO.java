package com.stu.entity;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class DormitoryOrderDTO {
    @NotNull(message = "地址ID不能为空")
    private Long addressId;

    @NotNull(message = "预约时间不能为空")
    @Future(message = "预约时间必须是将来的时间")
    private Date scheduledTime;

    private List<String> images;

    @NotBlank(message = "宿舍楼不能为空")
    private String dormitoryBuilding;

    @NotBlank(message = "房间范围不能为空")
    private String roomRange;

    @NotEmpty(message = "至少选择一个回收品类")
    private List<String> recyclingCategories;

    @NotBlank(message = "联系人不能为空")
    private String contactPerson;

    @NotBlank(message = "联系人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "请输入正确的手机号")
    private String contactPhone;

    private String schoolName;
    private String department;

    // 新增：前端可选传入的物品明细（统一写入订单items字段）
    private List<Map<String, Object>> items;
}
