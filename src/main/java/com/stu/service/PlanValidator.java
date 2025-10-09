package com.stu.service;

import com.stu.entity.RecyclingPlanRequest;
import com.stu.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Set;

@Component
public class PlanValidator {

    private static final Set<String> CYCLES = Set.of("WEEKLY", "BIWEEKLY", "MONTHLY");

    public void validatePlanRequest(RecyclingPlanRequest request) {
        if (!StringUtils.hasText(request.getPlanName())) {
            throw new BusinessException("计划名称不能为空");
        }
        if (!StringUtils.hasText(request.getCycleType()) || !CYCLES.contains(request.getCycleType())) {
            throw new BusinessException("回收周期不合法，必须为WEEKLY/BIWEEKLY/MONTHLY");
        }
        if (!StringUtils.hasText(request.getItemsConfig())) {
            throw new BusinessException("回收物品配置不能为空");
        }
        LocalDate next = request.getNextScheduleDate();
        if (next != null && next.isBefore(LocalDate.now())) {
            throw new BusinessException("下次执行日期不能早于今天");
        }
        if (request.getTotalCycles() != null && request.getTotalCycles() <= 0) {
            throw new BusinessException("总执行次数必须为正整数");
        }
    }
}

