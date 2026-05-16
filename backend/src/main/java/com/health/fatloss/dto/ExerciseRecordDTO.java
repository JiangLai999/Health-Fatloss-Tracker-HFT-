package com.health.fatloss.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class ExerciseRecordDTO {
    @NotNull(message = "运动ID不能为空")
    private Long exerciseId;

    @NotNull(message = "记录日期不能为空")
    private LocalDate recordDate;

    @NotNull(message = "运动时长不能为空")
    private Integer duration;

    private String note;
}
