package com.health.fatloss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("goal")
public class Goal implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String goalType;

    private Double targetValue;

    private Double startValue;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer status;

    private String note;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
