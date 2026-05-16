package com.health.fatloss;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.health.fatloss.mapper")
public class FatLossApplication {
    public static void main(String[] args) {
        SpringApplication.run(FatLossApplication.class, args);
    }
}
