package com.stu.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.stu.entity.EnterpriseOrder;
import com.stu.entity.EnterpriseUser;
import com.stu.entity.RecyclingPlan;
import com.stu.entity.RecyclingPlanRequest;
import com.stu.mapper.EnterpriseOrderMapper;
import com.stu.mapper.RecyclingPlanMapper;
import com.stu.service.RecyclingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
public class RecyclingPlanServiceImpl implements RecyclingPlanService {

    @Autowired
    private RecyclingPlanMapper recyclingPlanMapper;
    @Autowired
    private EnterpriseOrderMapper enterpriseOrderMapper;

    @Override
    public RecyclingPlan createPlan(EnterpriseUser enterprise, RecyclingPlanRequest request) {
        RecyclingPlan plan = new RecyclingPlan();
        plan.setPlanNo(generatePlanNo());
        plan.setEnterpriseId(enterprise.getId());
        plan.setPlanName(request.getPlanName());
        plan.setCycleType(request.getCycleType());
        plan.setTotalCycles(request.getTotalCycles());
        plan.setCompletedCycles(0);
        if (request.getNextScheduleDate() != null) {
            plan.setNextScheduleDate(java.sql.Date.valueOf(request.getNextScheduleDate()));
        }
        if (request.getEndDate() != null) {
            plan.setEndDate(java.sql.Date.valueOf(request.getEndDate()));
        }
        plan.setItemsConfig(request.getItemsConfig());
        plan.setInvoiceConfig(request.getInvoiceConfig());
        plan.setStatus("ACTIVE");
        recyclingPlanMapper.insert(plan);

        if (Boolean.TRUE.equals(request.getStartImmediately())) {
            EnterpriseOrder order = new EnterpriseOrder();
            order.setOrderNo("E" + IdWorker.getIdStr());
            order.setEnterpriseId(enterprise.getId());
            order.setOrderType("REGULAR");
            order.setPlanId(plan.getId());
            order.setStatus("PENDING");
            LocalDate next = request.getNextScheduleDate();
            if (next != null) {
                order.setScheduleTime(java.sql.Timestamp.valueOf(next.atStartOfDay()));
            } else {
                order.setScheduleTime(new Date());
            }
            enterpriseOrderMapper.insert(order);
        }
        return plan;
    }

    private String generatePlanNo() {
        return "P" + IdWorker.getIdStr();
    }
}

