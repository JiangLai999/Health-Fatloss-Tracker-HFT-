package com.health.fatloss.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.fatloss.common.Result;
import com.health.fatloss.dto.ExerciseRecordDTO;
import com.health.fatloss.entity.Exercise;
import com.health.fatloss.entity.ExerciseRecord;
import com.health.fatloss.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exercise")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/list")
    public Result<Page<Exercise>> getExerciseList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Exercise> exercisePage = exerciseService.getExerciseList(keyword, category, page, size);
        return Result.success(exercisePage);
    }

    @PostMapping
    public Result<String> addExercise(@RequestBody Exercise exercise) {
        exerciseService.addExercise(exercise);
        return Result.success("添加成功");
    }

    @PostMapping("/record")
    public Result<String> addExerciseRecord(@Valid @RequestBody ExerciseRecordDTO exerciseRecordDTO) {
        Long userId = getCurrentUserId();
        exerciseService.addExerciseRecord(userId, exerciseRecordDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/record")
    public Result<List<Map<String, Object>>> getExerciseRecordList(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate recordDate) {
        Long userId = getCurrentUserId();
        List<Map<String, Object>> exerciseRecordList = exerciseService.getExerciseRecordList(userId, recordDate);
        return Result.success(exerciseRecordList);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getExerciseStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Long userId = getCurrentUserId();
        Map<String, Object> statistics = exerciseService.getExerciseStatistics(userId, startDate, endDate);
        return Result.success(statistics);
    }

    @DeleteMapping("/record/{id}")
    public Result<String> deleteExerciseRecord(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        exerciseService.deleteExerciseRecord(userId, id);
        return Result.success("删除成功");
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
