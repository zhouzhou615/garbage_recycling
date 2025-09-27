package com.stu.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("openid")
    private String openid;

    @TableField("unionid")
    private String unionid;

    @TableField("nickname")
    private String nickname;

    @TableField("avatar_url")
    private String avatarUrl;

    @TableField("identity_type")
    private Integer identityType;

    @TableField("school_name")
    private String schoolName;

    @TableField("department")
    private String department;

    @TableField(value = "extended_info", typeHandler = com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler.class)
    private String extendedInfo;

    @JsonIgnore
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private Date createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    @TableField("last_login_at")
    private Date lastLoginAt;
}