package com.health.fatloss.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.fatloss.common.BusinessException;
import com.health.fatloss.dto.GoalDTO;
import com.health.fatloss.entity.BodyData;
import com.health.fatloss.entity.Goal;
import com.health.fatloss.mapper.GoalMapper;
import com.health.fatloss.service.BodyDataService;
import com.health.fatloss.service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoalServiceImpl extends ServiceImpl<GoalMapper, Goal> implements GoalService {

    @Autowired
    private BodyDataService bodyDataService;

    @Override
    @Transactional
    public void addGoal(Long userId, GoalDTO goalDTO) {
        Goal goal = new Goal();
        BeanUtil.copyProperties(goalDTO, goal);
        goal.setUserId(userId);
        goal.setStatus(0);
        
        if (goalDTO.getStartValue() == null && "weight".equals(goalDTO.getGoalType())) {
            LambdaQueryWrapper<BodyData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BodyData::getUserId, userId)
                    .orderByDesc(BodyData::getRecordDate)
                    .last("LIMIT 1");
            BodyData latestData = bodyDataService.getOne(queryWrapper);
            if (latestData != null) {
                goal.setStartValue(latestData.getWeight());
            }
        }
        
        this.save(goal);
    }

    @Override
    public List<Goal> getGoalList(Long userId, Integer status) {
        LambdaQueryWrapper<Goal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goal::getUserId, userId);
        if (status != null) {
            queryWrapper.eq(Goal::getStatus, status);
        }
        queryWrapper.orderByDesc(Goal::getCreateTime);
        return this.list(queryWrapper);
    }

    @Override
    public Map<String, Object> getGoalProgress(Long userId, Long goalId) {
        Goal goal = this.getById(goalId);
        if (goal == null) {
            throw new BusinessException("目标不存在");
        }
        if (!goal.getUserId().equals(userId)) {
            throw new BusinessException("无权查看此目标");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("goal", goal);

        long totalDays = ChronoUnit.DAYS.between(goal.getStartDate(), goal.getEndDate()) + 1;
        long passedDays = ChronoUnit.DAYS.between(goal.getStartDate(), LocalDate.now()) + 1;
        passedDays = Math.max(0, Math.min(passedDays, totalDays));
        
        result.put("totalDays", totalDays);
        result.put("passedDays", passedDays);
        result.put("progressPercent", (int) (passedDays * 100.0 / totalDays));

        if ("weight".equals(goal.getGoalType())) {
            LambdaQueryWrapper<BodyData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BodyData::getUserId, userId)
                    .orderByDesc(BodyData::getRecordDate)
                    .last("LIMIT 1");
            BodyData latestData = bodyDataService.getOne(queryWrapper);
            
            if (latestData != null && goal.getStartValue() != null) {
                double currentValue = latestData.getWeight();
                double startValue = goal.getStartValue();
                double targetValue = goal.getTargetValue();
                
                double totalChange = Math.abs(targetValue - startValue);
                double currentChange = Math.abs(currentValue - startValue);
                double progress = totalChange > 0 ? (currentChange / totalChange) * 100 : 0;
                
                result.put("currentValue", currentValue);
                result.put("totalChange", startValue - targetValue);
                result.put("currentChange", startValue - currentValue);
                result.put("valueProgress", Math.min(100, progress));
            }
        }

        return result;
    }

    @Override
    @Transactional
    public void updateGoalStatus(Long userId, Long goalId, Integer status) {
        Goal goal = this.getById(goalId);
        if (goal == null) {
            throw new BusinessException("目标不存在");
        }
        if (!goal.getUserId().equals(userId)) {
            throw new BusinessException("无权修改此目标");
        }
        
        goal.setStatus(status);
        this.updateById(goal);
    }

    @Override
    @Transactional
    public void deleteGoal(Long userId, Long goalId) {
        Goal goal = this.getById(goalId);
        if (goal == null) {
            throw new BusinessException("目标不存在");
        }
        if (!goal.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此目标");
        }
        this.removeById(goalId);
    }
}
