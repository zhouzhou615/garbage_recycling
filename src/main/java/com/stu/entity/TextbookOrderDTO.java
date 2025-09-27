package com.stu.entity;


import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.Date;
import java.util.List;

@Data
public class TextbookOrderDTO {

    @NotNull(message = "地址ID不能为空")
    private  Long addressId;

    @NotNull(message = "预约时间不能为空")
    @Future(message = "预约时间必须是未来的时间")
    private Date scheduledTime;

    private List<String> images;

    @NotBlank(message = "教材类型不能为空")
    private String textbookType;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量至少为1")
    private Integer quantity;

    private String unit;

    @NotBlank(message = "新旧程度不能为空")
    private String condition;

    @NotBlank(message = "取件地点不能为空")
    private String pickupLocation;

    private String schoolName;
    private String department;


}
