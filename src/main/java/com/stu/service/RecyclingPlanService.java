package com.stu.service;

import com.stu.entity.EnterpriseUser;
import com.stu.entity.RecyclingPlan;
import com.stu.entity.RecyclingPlanRequest;

public interface RecyclingPlanService {
    RecyclingPlan createPlan(EnterpriseUser enterprise, RecyclingPlanRequest request);
}

