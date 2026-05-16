package com.health.fatloss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("exercise")
public class Exercise implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String category;

    private Double caloriesPerMinute;

    private String description;

    private String image;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
