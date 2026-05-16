package com.health.fatloss.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class GoalDTO {
    @NotNull(message = "目标类型不能为空")
    private String goalType;

    @NotNull(message = "目标值不能为空")
    private Double targetValue;

    private Double startValue;

    @NotNull(message = "开始日期不能为空")
    private LocalDate startDate;

    @NotNull(message = "结束日期不能为空")
    private LocalDate endDate;

    private String note;
}
