package com.stu.service.impl;

import com.stu.service.OperationLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    private static final Logger log = LoggerFactory.getLogger(OperationLogServiceImpl.class);

    @Override
    public void logOrderCreation(Long enterpriseId, Long orderId, String type) {
        log.info("[EnterpriseOrderLog] enterpriseId={}, orderId={}, type={}", enterpriseId, orderId, type);
    }
}

