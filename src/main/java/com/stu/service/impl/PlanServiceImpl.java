package com.stu.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.stu.entity.EnterpriseOrder;
import com.stu.entity.RecyclingPlan;
import com.stu.mapper.EnterpriseOrderMapper;
import com.stu.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PlanServiceImpl implements PlanService {

    @Autowired
    private EnterpriseOrderMapper enterpriseOrderMapper;

    @Override
    public void generateNextOrder(RecyclingPlan plan) {
        EnterpriseOrder order = new EnterpriseOrder();
        order.setOrderNo("E" + IdWorker.getIdStr());
        order.setEnterpriseId(plan.getEnterpriseId());
        order.setOrderType("REGULAR");
        order.setPlanId(plan.getId());
        order.setStatus("PENDING");
        if (plan.getNextScheduleDate() != null) {
            order.setScheduleTime(new Date(plan.getNextScheduleDate().getTime()));
        } else {
            order.setScheduleTime(new Date());
        }
        enterpriseOrderMapper.insert(order);
    }
}
