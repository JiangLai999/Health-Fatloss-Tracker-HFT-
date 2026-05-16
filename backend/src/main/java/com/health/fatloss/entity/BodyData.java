package com.health.fatloss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("body_data")
public class BodyData implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private LocalDate recordDate;

    private Double weight;

    private Double bmi;

    private Double bodyFatRate;

    private Double muscleMass;

    private Double waterContent;

    private String note;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
