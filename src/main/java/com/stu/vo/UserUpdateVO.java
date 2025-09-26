package com.stu.vo;

import lombok.Data;


@Data
public class UserUpdateVO {

    private String nickname;

    private String avatarUrl;

    private Integer identityType;

    private String schoolName;

    private String department;

    private String extendedInfo;
}