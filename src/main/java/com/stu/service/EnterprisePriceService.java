package com.stu.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stu.config.PriceConfig;
import com.stu.entity.OrderItem;
import com.stu.entity.WeightUnit;
import com.stu.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EnterprisePriceService {

    @Autowired
    private PriceConfig priceConfig;
    @Autowired
    private ObjectMapper objectMapper;

    public BigDecimal calculateEnterpriseAmount(List<OrderItem> items, BigDecimal totalWeight, WeightUnit weightUnit) {
        try {
            BigDecimal baseAmount = calculateBaseAmount(items, totalWeight);
            BigDecimal weightInKg = convertToKg(totalWeight, weightUnit);
            BigDecimal discount = calculateBulkDiscount(weightInKg);
            return baseAmount.multiply(discount);
        } catch (Exception e) {
            throw new BusinessException("价格计算失败，请稍后重试");
        }
    }

    private BigDecimal calculateBaseAmount(List<OrderItem> items, BigDecimal totalWeight) {
        if (totalWeight == null) return BigDecimal.ZERO;
        // 简化：按废纸基础价估算一口价，后续由运营覆盖
        BigDecimal pricePerKg = priceConfig.getPrice("waste_paper", true);
        if (pricePerKg == null) pricePerKg = BigDecimal.ZERO;
        return pricePerKg.multiply(convertToKg(totalWeight, WeightUnit.KG));
    }

    private BigDecimal convertToKg(BigDecimal weight, WeightUnit unit) {
        if (unit == WeightUnit.TON) {
            return weight.multiply(new BigDecimal("1000"));
        }
        return weight;
    }

    private BigDecimal calculateBulkDiscount(BigDecimal weightInKg) {
        if (weightInKg.compareTo(new BigDecimal("10000")) >= 0) {
            return new BigDecimal("0.85");
        } else if (weightInKg.compareTo(new BigDecimal("5000")) >= 0) {
            return new BigDecimal("0.9");
        } else if (weightInKg.compareTo(new BigDecimal("1000")) >= 0) {
            return new BigDecimal("0.95");
        }
        return BigDecimal.ONE;
    }
}

