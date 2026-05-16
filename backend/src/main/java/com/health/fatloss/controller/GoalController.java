package com.health.fatloss.controller;

import com.health.fatloss.common.Result;
import com.health.fatloss.dto.GoalDTO;
import com.health.fatloss.entity.Goal;
import com.health.fatloss.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goal")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping
    public Result<String> addGoal(@Valid @RequestBody GoalDTO goalDTO) {
        Long userId = getCurrentUserId();
        goalService.addGoal(userId, goalDTO);
        return Result.success("添加成功");
    }

    @GetMapping
    public Result<List<Goal>> getGoalList(@RequestParam(required = false) Integer status) {
        Long userId = getCurrentUserId();
        List<Goal> goalList = goalService.getGoalList(userId, status);
        return Result.success(goalList);
    }

    @GetMapping("/{id}/progress")
    public Result<Map<String, Object>> getGoalProgress(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        Map<String, Object> progress = goalService.getGoalProgress(userId, id);
        return Result.success(progress);
    }

    @PutMapping("/{id}/status")
    public Result<String> updateGoalStatus(@PathVariable Long id, @RequestParam Integer status) {
        Long userId = getCurrentUserId();
        goalService.updateGoalStatus(userId, id, status);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteGoal(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        goalService.deleteGoal(userId, id);
        return Result.success("删除成功");
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getPrincipal();
    }
}
