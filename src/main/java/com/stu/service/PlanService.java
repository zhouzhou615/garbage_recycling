package com.stu.service;

import com.stu.entity.RecyclingPlan;

public interface PlanService {
    void generateNextOrder(RecyclingPlan plan);
}
