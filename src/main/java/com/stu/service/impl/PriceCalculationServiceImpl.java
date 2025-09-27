package com.stu.service.impl;

import com.stu.config.PriceConfig;
import com.stu.entity.DormitoryOrderDTO;
import com.stu.entity.TextbookOrderDTO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceCalculationServiceImpl {
    @Autowired
    private PriceConfig priceConfig;
    @Value("${recycling.pilot.period:true}")
    private boolean isPilotPeriod;

    public BigDecimal calculateTextbookAmount(TextbookOrderDTO orderDTO) {
        //教材基础价格
        BigDecimal basePrice = priceConfig.getPrice("waste_paper", isPilotPeriod);
        //根据新旧程度调整价格
        BigDecimal conditionFactor = getConditionFactor(orderDTO.getCondition());
        //计算总金额=基础价格 * 数量 * 新旧程度系数
        return basePrice.multiply(new  BigDecimal(orderDTO.getQuantity())).multiply(conditionFactor);
    }
    public BigDecimal calculateDormitoryAmount(DormitoryOrderDTO orderDTO) {
        BigDecimal totalAmount = BigDecimal.ZERO;// 初始化总金额
        // 遍历所有回收品类，累加计算总金额
        for (String category : orderDTO.getRecyclingCategories()) {
            // 获取品类价格
            BigDecimal price = priceConfig.getPrice(getCategoryKey(category), isPilotPeriod);
            // 预估重量计算
            BigDecimal estimatedWeight = estimateWeightByCategory(category);
            totalAmount = totalAmount.add(price.multiply(estimatedWeight));
        }
        return totalAmount;
    }

    // 新增：新旧程度系数方法
    private BigDecimal getConditionFactor(String condition) {
        switch (condition) {
            case "全新": return new BigDecimal("1.2");
            case "九成新": return new BigDecimal("1.0");
            case "七成新": return new BigDecimal("0.8");
            case "五成新": return new BigDecimal("0.6");
            case "三成新": return new BigDecimal("0.4");
            default: return new BigDecimal("0.3");// 其他情况按0.3计算
        }
    }
    /**
     * 将中文品类名称转换为配置键
     */
    private String getCategoryKey(String chineseName) {
        switch (chineseName) {
            case "废纸": case "纸板": return "waste_paper";
            case "塑料瓶": case "塑料容器": return "plastic_bottle";
            case "旧衣物": return "old_clothes";
            case "电子产品": return "electronics";
            default: return "other";
        }
    }
    /**
     * 根据品类预估重量（公斤）
     */
    private BigDecimal estimateWeightByCategory(String category) {
        switch (category) {
            case "废纸": case "纸板": return new BigDecimal("2.5");
            case "塑料瓶": case "塑料容器": return new BigDecimal("1.8");
            case "旧衣物": return new BigDecimal("3.0");
            case "电子产品": return new BigDecimal("0.5");
            default: return new BigDecimal("1.0");
        }
    }
}
