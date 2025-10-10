package com.stu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@Schema(description = "用户实体类，存储系统用户的基本信息（含微信授权信息、身份信息等）")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户ID（自增主键）", example = "1001")
    private Long id;

    @TableField("openid")
    @Schema(description = "微信开放平台唯一标识（用于微信登录用户）", example = "o6_bmjrPTlm6_2sgVt7hMZOPfL2M")
    private String openid;

    @TableField("unionid")
    @Schema(description = "微信UnionID（多公众号/小程序统一用户标识，非必填）", example = "o6_bmasdasdsad6_2sgVt7hMZOPfL")
    private String unionid;

    @TableField("nickname")
    @Schema(description = "用户昵称（微信昵称或自定义名称）", example = "张三")
    private String nickname;

    @TableField("avatar_url")
    @Schema(description = "用户头像URL", example = "https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLkibWiaib4YQibC9ibiaibiaibiaibiaibiaibiaibiaibia/132")
    private String avatarUrl;

    @TableField("identity_type")
    @Schema(description = "身份类型（1-个人用户，2-企业用户，3-管理员，4-回收员）", example = "1")
    private Integer identityType;

    @TableField("school_name")
    @Schema(description = "学校名称（仅学生/教职工用户填写）", example = "清华大学")
    private String schoolName;

    @TableField("department")
    @Schema(description = "院系/部门（仅学生/教职工/企业用户填写）", example = "计算机科学与技术系")
    private String department;

    @TableField(value = "extended_info", typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    @Schema(description = "扩展信息（JSON格式，存储个性化数据，如学生学号、企业编号等）",
            example = "{\"studentId\":\"2024001001\",\"grade\":\"大三\"}")
    private String extendedInfo;

    @TableField("user_type")
    @Schema(description = "用户类型：personal-个人，campus-校园，enterprise-企业", example = "enterprise")
    private String userType;

    @TableField("company_name")
    @Schema(description = "公司名称（企业用户必填）", example = "某某环保科技有限公司")
    private String companyName;

    @TableField("unified_social_credit_code")
    @Schema(description = "统一社会信用代码（企业用户必填）", example = "91310000MA1K123456")
    private String unifiedSocialCreditCode;

    @TableField("invoice_title")
    @Schema(description = "发票抬头（企业用户开票使用）", example = "某某环保科技有限公司")
    private String invoiceTitle;

    @TableField("tax_number")
    @Schema(description = "税号/纳税人识别号（企业用户开票使用）", example = "91310000MA1K123456")
    private String taxNumber;

    @JsonIgnore
    @TableField("deleted")
    @TableLogic
    @Schema(description = "逻辑删除标识（0-未删除，1-已删除），前端无需处理", example = "0", hidden = true)
    private Integer deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Schema(description = "用户创建时间（首次注册时间）", example = "2024-09-01 08:30:00")
    private Date createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "信息更新时间（资料修改时自动更新）", example = "2024-09-15 14:20:00")
    private Date updatedAt;

    @TableField("last_login_at")
    @Schema(description = "最后登录时间", example = "2024-09-26 20:10:30")
    private Date lastLoginAt;
}
