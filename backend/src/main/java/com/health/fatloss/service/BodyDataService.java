package com.health.fatloss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.health.fatloss.dto.BodyDataDTO;
import com.health.fatloss.entity.BodyData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface BodyDataService extends IService<BodyData> {
    void addBodyData(Long userId, BodyDataDTO bodyDataDTO);
    List<BodyData> getBodyDataList(Long userId, LocalDate startDate, LocalDate endDate);
    Map<String, Object> getBodyDataStatistics(Long userId);
    void deleteBodyData(Long userId, Long bodyDataId);
}
