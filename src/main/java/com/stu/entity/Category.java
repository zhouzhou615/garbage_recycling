package com.stu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;

@Data
@TableName("category")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
    private Boolean isActive;

    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;
}