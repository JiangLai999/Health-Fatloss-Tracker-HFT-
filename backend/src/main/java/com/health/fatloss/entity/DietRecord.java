package com.health.fatloss.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("diet_record")
public class DietRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long foodId;

    private LocalDate recordDate;

    private String mealType;

    private Double amount;

    private Double calories;

    private Double protein;

    private Double carbohydrate;

    private Double fat;

    private String note;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
