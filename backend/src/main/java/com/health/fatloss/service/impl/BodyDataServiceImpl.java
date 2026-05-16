package com.health.fatloss.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.health.fatloss.common.BusinessException;
import com.health.fatloss.dto.BodyDataDTO;
import com.health.fatloss.entity.BodyData;
import com.health.fatloss.entity.User;
import com.health.fatloss.mapper.BodyDataMapper;
import com.health.fatloss.service.BodyDataService;
import com.health.fatloss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BodyDataServiceImpl extends ServiceImpl<BodyDataMapper, BodyData> implements BodyDataService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public void addBodyData(Long userId, BodyDataDTO bodyDataDTO) {
        BodyData bodyData = new BodyData();
        BeanUtil.copyProperties(bodyDataDTO, bodyData);
        bodyData.setUserId(userId);

        if (bodyDataDTO.getWeight() != null) {
            User user = userService.getById(userId);
            if (user != null && user.getHeight() != null) {
                double heightInMeters = user.getHeight() / 100.0;
                double bmi = bodyDataDTO.getWeight() / (heightInMeters * heightInMeters);
                bodyData.setBmi(BigDecimal.valueOf(bmi).setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
        }

        this.save(bodyData);
    }

    @Override
    public List<BodyData> getBodyDataList(Long userId, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<BodyData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BodyData::getUserId, userId);
        if (startDate != null) {
            queryWrapper.ge(BodyData::getRecordDate, startDate);
        }
        if (endDate != null) {
            queryWrapper.le(BodyData::getRecordDate, endDate);
        }
        queryWrapper.orderByDesc(BodyData::getRecordDate);
        return this.list(queryWrapper);
    }

    @Override
    public Map<String, Object> getBodyDataStatistics(Long userId) {
        Map<String, Object> result = new HashMap<>();

        LambdaQueryWrapper<BodyData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BodyData::getUserId, userId)
                .orderByDesc(BodyData::getRecordDate)
                .last("LIMIT 1");
        BodyData latestData = this.getOne(queryWrapper);

        if (latestData != null) {
            result.put("currentWeight", latestData.getWeight());
            result.put("currentBmi", latestData.getBmi());
            result.put("currentBodyFatRate", latestData.getBodyFatRate());
            result.put("currentMuscleMass", latestData.getMuscleMass());
        }

        LambdaQueryWrapper<BodyData> firstQueryWrapper = new LambdaQueryWrapper<>();
        firstQueryWrapper.eq(BodyData::getUserId, userId)
                .orderByAsc(BodyData::getRecordDate)
                .last("LIMIT 1");
        BodyData firstData = this.getOne(firstQueryWrapper);

        if (firstData != null && latestData != null) {
            result.put("weightChange", latestData.getWeight() - firstData.getWeight());
        }

        return result;
    }

    @Override
    @Transactional
    public void deleteBodyData(Long userId, Long bodyDataId) {
        BodyData bodyData = this.getById(bodyDataId);
        if (bodyData == null) {
            throw new BusinessException("记录不存在");
        }
        if (!bodyData.getUserId().equals(userId)) {
            throw new BusinessException("无权删除此记录");
        }
        this.removeById(bodyDataId);
    }
}
