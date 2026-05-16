package com.health.fatloss.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class DietRecordDTO {
    @NotNull(message = "食物ID不能为空")
    private Long foodId;

    @NotNull(message = "记录日期不能为空")
    private LocalDate recordDate;

    @NotNull(message = "餐次类型不能为空")
    private String mealType;

    @NotNull(message = "食物数量不能为空")
    private Double amount;

    private String note;
}
