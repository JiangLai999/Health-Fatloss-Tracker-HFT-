package com.health.fatloss.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class BodyDataDTO {
    @NotNull(message = "记录日期不能为空")
    private LocalDate recordDate;

    @NotNull(message = "体重不能为空")
    private Double weight;

    private Double bodyFatRate;
    private Double muscleMass;
    private Double waterContent;
    private String note;
}
