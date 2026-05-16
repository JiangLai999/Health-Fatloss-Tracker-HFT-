package com.health.fatloss.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.health.fatloss.common.Result;
import com.health.fatloss.dto.DietRecordDTO;
import com.health.fatloss.entity.DietRecord;
import com.health.fatloss.entity.Food;
import com.health.fatloss.service.DietService;
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
@RequestMapping("/diet")
public class DietController {

    @Autowired
    private DietService dietService;

    @GetMapping("/food")
    public Result<Page<Food>> getFoodList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Food> foodPage = dietService.getFoodList(keyword, category, page, size);
        return Result.success(foodPage);
    }

    @PostMapping("/food")
    public Result<String> addFood(@RequestBody Food food) {
        dietService.addFood(food);
        return Result.success("添加成功");
    }

    @PostMapping("/record")
    public Result<String> addDietRecord(@Valid @RequestBody DietRecordDTO dietRecordDTO) {
        Long userId = getCurrentUserId();
        dietService.addDietRecord(userId, dietRecordDTO);
        return Result.success("添加成功");
    }

    @GetMapping("/record")
    public Result<List<DietRecord>> getDietRecordList(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate recordDate) {
        Long userId = getCurrentUserId();
        List<DietRecord> dietRecordList = dietService.getDietRecordList(userId, recordDate);
        return Result.success(dietRecordList);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getDietStatistics(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Long userId = getCurrentUserId();
        Map<String, Object> statistics = dietService.getDietStatistics(userId, startDate, endDate);
        return Result.success(statistics);
    }

    @DeleteMapping("/record/{id}")
    public Result<String> deleteDietRecord(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        dietService.deleteDietRecord(userId, id);
        return Result.success("删除成功");
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
