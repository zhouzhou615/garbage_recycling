package com.stu.service;

public interface OperationLogService {
    void logOrderCreation(Long enterpriseId, Long orderId, String type);
}

