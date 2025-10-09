package com.stu.vo;

import com.stu.entity.RecyclingPlan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "企业回收计划响应模型")
public class RecyclingPlanResponse {
    private Long id;
    private String planNo;
    private String planName;
    private String cycleType;
    private String status;

    public static RecyclingPlanResponse fromPlan(RecyclingPlan plan) {
        RecyclingPlanResponse r = new RecyclingPlanResponse();
        r.setId(plan.getId());
        r.setPlanNo(plan.getPlanNo());
        r.setPlanName(plan.getPlanName());
        r.setCycleType(plan.getCycleType());
        r.setStatus(plan.getStatus());
        return r;
    }
}

