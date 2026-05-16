package com.health.fatloss.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.fatloss.common.BusinessException;
import com.health.fatloss.dto.DietRecordDTO;
import com.health.fatloss.entity.DietRecord;
import com.health.fatloss.entity.Food;
import com.health.fatloss.mapper.DietRecordMapper;
import com.health.fatloss.mapper.FoodMapper;
import com.health.fatloss.service.DietService;
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
public class DietServiceImpl extends ServiceImpl<FoodMapper, Food> implements DietService {

    @Autowired
    private DietRecordMapper dietRecordMapper;

    @Override
    public Page<Food> getFoodList(String keyword, String category, Integer page, Integer size) {
        Page<Food> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Food> queryWrapper = new LambdaQueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Food::getName, keyword);
        }
        if (StringUtils.hasText(category)) {
            queryWrapper.eq(Food::getCategory, category);
        }
        queryWrapper.orderByDesc(Food::getCreateTime);
        
        return this.page(pageParam, queryWrapper);
    }

    @Override
    @Transactional
    public void addFood(Food food) {
        this.save(food);
    }

    @Override
    @Transactional
    public void addDietRecord(Long userId, DietRecordDTO dietRecordDTO) {
        Food food = this.getById(dietRecordDTO.getFoodId());
        if (food == null) {
            throw new BusinessException("食物不存在");
        }

        DietRecord dietRecord = new DietRecord();
        BeanUtil.copyProperties(dietRecordDTO, dietRecord);
        dietRecord.setUserId(userId);

        double ratio = dietRecordDTO.getAmount() / 100.0;
        dietRecord.setCalories(BigDecimal.valueOf(food.getCalories() * ratio).setScale(2, RoundingMode.HALF_UP).doubleValue());
        dietRecord.setProtein(food.getProtein() != null ? BigDecimal.valueOf(food.getProtein() * ratio).setScale(2, RoundingMode.HALF_UP).doubleValue() : null);
        dietRecord.setCarbohydrate(food.getCarbohydrate() != null ? BigDecimal.valueOf(food.getCarbohydrate() * ratio).setScale(2, RoundingMode.HALF_UP).doubleValue() : null);
        dietRecord.setFat(food.getFat() != null ? BigDecimal.valueOf(food.getFat() * ratio).setScale(2, RoundingMode.HALF_UP).doubleValue() : null);

        dietRecordMapper.insert(dietRecord);
    }

    @Override
    public List<DietRecord> getDietRecordList(Long userId, LocalDate recordDate) {
        LambdaQueryWrapper<DietRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DietRecord::getUserId, userId);
        if (recordDate != null) {
            queryWrapper.eq(DietRecord::getRecordDate, recordDate);
        }
        queryWrapper.orderByDesc(DietRecord::getRecordDate, DietRecord::getCreateTime);
        return dietRecordMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> getDietStatistics(Long userId, LocalDate startDate, LocalDate endDate) {
        Map<String, Object> result = new HashMap<>();

        LambdaQueryWrapper<DietRecord> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DietRecord::getUserId, userId);
        if (startDate != null) {
            queryWrapper.ge(DietRecord::getRecordDate, startDate);
        }
        if (endDate != null) {
            queryWrapper.le(DietRecord::getRecordDate, endDate);
        }

        List<DietRecord> records = dietRecordMapper.selectList(queryWrapper);

        double totalCalories = 0;
        double totalProtein = 0;
        double totalCarbohydrate = 0;
        double totalFat = 0;

        for (DietRecord record : records) {
            totalCalories += record.getCalories() != null ? record.getCalories() : 0;
            totalProtein += record.getProtein() != null ? record.getProtein() : 0;
            totalCarbohydrate += record.getCarbohydrate() != null ? record.getCarbohydrate() : 0;
            totalFat += record.getFat() != null ? record.getFat() : 0;
        }

        result.put("totalCalories", BigDecimal.valueOf(totalCalories).setScale(2, RoundingMode.HALF_UP).doubleValue());
        result.put("totalProtein", BigDecimal.valueOf(totalProtein).setScale(2, RoundingMode.HALF_UP).doubleValue());
        result.put("totalCarbohydrate", BigDecimal.valueOf(totalCarbohydrate).setScale(2, RoundingMode.HALF_UP).doubleValue());
        result.put("totalFat", BigDecimal.valueOf(totalFat).setScale(2, RoundingMode.HALF_UP).doubleValue());
        result.put("recordCount", records.size());

        return result;
    }

    @Override
    @Transactional
    public void deleteDietRecord(Long userId, Long recordId) {
        DietRecord dietRecord = dietRecordMapper.selectById(recordId);
        if (dietRecord == null) {
            throw new BusinessException("记录不存在");
        }
        if (!dietRecord.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此记录");
        }
        dietRecordMapper.deleteById(recordId);
    }
}
