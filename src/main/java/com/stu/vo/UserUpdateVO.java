package com.stu.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "用户信息更新DTO，用于接收用户信息更新请求参数")
public class UserUpdateVO {

    @Schema(description = "用户昵称", example = "李四")
    private String nickname;

    @Schema(description = "用户头像URL", example = "https://thirdwx.qlogo.cn/mmopen/yyy")
    private String avatarUrl;

    @Schema(description = "身份类型（1-个人用户，2-企业用户，3-管理员）", example = "1")
    private Integer identityType;

    @Schema(description = "学校名称（仅学生/教职工填写）", example = "清华大学")
    private String schoolName;

    @Schema(description = "院系/部门（仅学生/教职工填写）", example = "电子工程系")
    private String department;

    @Schema(description = "扩展信息（JSON格式）", example = "{\"studentId\":\"2024002\"}")
    private String extendedInfo;
}