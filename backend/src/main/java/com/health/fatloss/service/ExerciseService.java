package com.health.fatloss.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.health.fatloss.dto.ExerciseRecordDTO;
import com.health.fatloss.entity.Exercise;
import com.health.fatloss.entity.ExerciseRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ExerciseService extends IService<Exercise> {
    Page<Exercise> getExerciseList(String keyword, String category, Integer page, Integer size);
    void addExercise(Exercise exercise);
    
    void addExerciseRecord(Long userId, ExerciseRecordDTO exerciseRecordDTO);
    List<Map<String, Object>> getExerciseRecordList(Long userId, LocalDate recordDate);
    Map<String, Object> getExerciseStatistics(Long userId, LocalDate startDate, LocalDate endDate);
    void deleteExerciseRecord(Long userId, Long recordId);
}
