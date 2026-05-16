package com.health.fatloss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("exercise_record")
public class ExerciseRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long exerciseId;

    private LocalDate recordDate;

    private Integer duration;

    private Double calories;

    private String note;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
