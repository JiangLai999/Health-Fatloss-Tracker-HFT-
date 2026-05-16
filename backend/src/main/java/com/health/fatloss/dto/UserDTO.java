package com.health.fatloss.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private Integer gender;
    private Integer age;
    private Double height;
    private Double weight;
    private Integer activityLevel;
}
