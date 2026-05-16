package com.health.fatloss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.fatloss.dto.GoalDTO;
import com.health.fatloss.entity.Goal;

import java.util.List;
import java.util.Map;

public interface GoalService extends IService<Goal> {
    void addGoal(Long userId, GoalDTO goalDTO);
    List<Goal> getGoalList(Long userId, Integer status);
    Map<String, Object> getGoalProgress(Long userId, Long goalId);
    void updateGoalStatus(Long userId, Long goalId, Integer status);
    void deleteGoal(Long userId, Long goalId);
}
