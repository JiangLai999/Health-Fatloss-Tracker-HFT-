package com.health.fatloss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.fatloss.dto.DietRecordDTO;
import com.health.fatloss.entity.DietRecord;
import com.health.fatloss.entity.Food;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface DietService extends IService<Food> {
    Page<Food> getFoodList(String keyword, String category, Integer page, Integer size);
    void addFood(Food food);
    
    void addDietRecord(Long userId, DietRecordDTO dietRecordDTO);
    List<DietRecord> getDietRecordList(Long userId, LocalDate recordDate);
    Map<String, Object> getDietStatistics(Long userId, LocalDate startDate, LocalDate endDate);
    void deleteDietRecord(Long userId, Long recordId);
}
