package com.health.fatloss.controller;

import com.health.fatloss.common.Result;
import com.health.fatloss.dto.BodyDataDTO;
import com.health.fatloss.entity.BodyData;
import com.health.fatloss.service.BodyDataService;
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
@RequestMapping("/body")
public class BodyDataController {

    @Autowired
    private BodyDataService bodyDataService;

    @PostMapping
    public Result<String> addBodyData(@Valid @RequestBody BodyDataDTO bodyDataDTO) {
        Long userId = getCurrentUserId();
        bodyDataService.addBodyData(userId, bodyDataDTO);
        return Result.success("添加成功");
    }

    @GetMapping
    public Result<List<BodyData>> getBodyDataList(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Long userId = getCurrentUserId();
        List<BodyData> bodyDataList = bodyDataService.getBodyDataList(userId, startDate, endDate);
        return Result.success(bodyDataList);
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getBodyDataStatistics() {
        Long userId = getCurrentUserId();
        Map<String, Object> statistics = bodyDataService.getBodyDataStatistics(userId);
        return Result.success(statistics);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteBodyData(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        bodyDataService.deleteBodyData(userId, id);
        return Result.success("删除成功");
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
