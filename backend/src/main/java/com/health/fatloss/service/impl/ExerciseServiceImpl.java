package com.health.fatloss.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.fatloss.common.BusinessException;
import com.health.fatloss.dto.ExerciseRecordDTO;
import com.health.fatloss.entity.Exercise;
import com.health.fatloss.entity.ExerciseRecord;
import com.health.fatloss.mapper.ExerciseMapper;
import com.health.fatloss.mapper.ExerciseRecordMapper;
import com.health.fatloss.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExerciseServiceImpl extends ServiceImpl<ExerciseMapper, Exercise> implements ExerciseService {

    @Autowired
    private ExerciseRecordMapper exerciseRecordMapper;

    @Override
    public Page<Exercise> getExerciseList(String keyword, String category, Integer page, Integer size) {
        Page<Exercise> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Exercise> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Exercise::getName, keyword);
        }
        if (StringUtils.hasText(category)) {
            queryWrapper.eq(Exercise::getCategory, category);
        }
        queryWrapper.orderByDesc(Exercise::getCreateTime);
        
        return this.page(pageParam, queryWrapper);
    }

    @Override
    @Transactional
    public void addExercise(Exercise exercise) {
        this.save(exercise);
    }

    @Override
    @Transactional
    public void addExerciseRecord(Long userId, ExerciseRecordDTO exerciseRecordDTO) {
        Exercise exercise = this.getById(exerciseRecordDTO.getExerciseId());
        if (exercise == null) {
            throw new BusinessException("运动不存在");
        }

        ExerciseRecord exerciseRecord = new ExerciseRecord();
        BeanUtil.copyProperties(exerciseRecordDTO, exerciseRecord);
        exerciseRecord.setUserId(userId);

        double calories = exercise.getCaloriesPerMinute() * exerciseRecordDTO.getDuration();
        exerciseRecord.setCalories(BigDecimal.valueOf(calories).setScale(2, RoundingMode.HALF_UP).doubleValue());

        exerciseRecordMapper.insert(exerciseRecord);
    }

    @Override
    public List<Map<String, Object>> getExerciseRecordList(Long userId, LocalDate recordDate) {
        LambdaQueryWrapper<ExerciseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExerciseRecord::getUserId, userId);
        if (recordDate != null) {
            queryWrapper.eq(ExerciseRecord::getRecordDate, recordDate);
        }
        queryWrapper.orderByDesc(ExerciseRecord::getRecordDate, ExerciseRecord::getCreateTime);
        
        List<ExerciseRecord> records = exerciseRecordMapper.selectList(queryWrapper);
        
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (ExerciseRecord record : records) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", record.getId());
            map.put("exerciseId", record.getExerciseId());
            map.put("recordDate", record.getRecordDate());
            map.put("duration", record.getDuration());
            map.put("calories", record.getCalories());
            map.put("note", record.getNote());
            map.put("createTime", record.getCreateTime());
            
            Exercise exercise = this.getById(record.getExerciseId());
            if (exercise != null) {
                map.put("exerciseName", exercise.getName());
                map.put("category", exercise.getCategory());
                map.put("caloriesPerMinute", exercise.getCaloriesPerMinute());
            }
            
            result.add(map);
        }
        return result;
    }

    @Override
    public Map<String, Object> getExerciseStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();

        LambdaQueryWrapper<ExerciseRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ExerciseRecord::getUserId, userId);
        if (startDate != null) {
            queryWrapper.ge(ExerciseRecord::getRecordDate, startDate);
        }
        if (endDate != null) {
            queryWrapper.le(ExerciseRecord::getRecordDate, endDate);
        }

        List<ExerciseRecord> records = exerciseRecordMapper.selectList(queryWrapper);

        double totalCalories = 0;
        int totalDuration = 0;

        for (ExerciseRecord record : records) {
            totalCalories += record.getCalories() != null ? record.getCalories() : 0;
            totalDuration += record.getDuration() != null ? record.getDuration() : 0;
        }

        result.put("totalCalories", BigDecimal.valueOf(totalCalories).setScale(2, RoundingMode.HALF_UP).doubleValue());
        result.put("totalDuration", totalDuration);
        result.put("recordCount", records.size());

        return result;
    }

    @Override
    @Transactional
    public void deleteExerciseRecord(Long userId, Long recordId) {
        ExerciseRecord exerciseRecord = exerciseRecordMapper.selectById(recordId);
        if (exerciseRecord == null) {
            throw new BusinessException("记录不存在");
        }
        if (!exerciseRecord.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此记录");
        }
        exerciseRecordMapper.deleteById(recordId);
    }
}
