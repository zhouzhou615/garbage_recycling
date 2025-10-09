package com.stu.service;

import com.stu.entity.*;
import java.math.BigDecimal;

public interface EnterpriseOrderService {
    EnterpriseOrder createBulkOrder(EnterpriseUser enterprise, BulkOrderRequest request, BigDecimal estimatedAmount);
}

