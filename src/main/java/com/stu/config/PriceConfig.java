package com.stu.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "recycling.price")
public class PriceConfig {

    // 试点期价格上浮比例（默认20%）
    private double pilotPremiumRate = 0.20;
    // 基础回收价格（元/公斤）
    private Map<String, BigDecimal> basePrices = new HashMap<>();
    // 试点期特殊价格
    private Map<String, BigDecimal> pilotPrices = new HashMap<>();

    // 构造函数中初始化价格
    public PriceConfig() {
        // 基础价格（完全按照商业计划书中的定价）
        basePrices.put("waste_paper", new BigDecimal("1.00"));
        basePrices.put("old_clothes", new BigDecimal("0.50"));
        basePrices.put("plastic_bottle", new BigDecimal("2.00"));
        basePrices.put("other_recyclable", BigDecimal.ZERO);

        basePrices.put("battery", BigDecimal.ZERO);
        basePrices.put("electronics", BigDecimal.ZERO);
        basePrices.put("medicine", BigDecimal.ZERO);
        basePrices.put("other_hazardous", BigDecimal.ZERO);

        basePrices.put("flower_plant", BigDecimal.ZERO);
        basePrices.put("small_bone", BigDecimal.ZERO);
        basePrices.put("leftover_food", BigDecimal.ZERO);
        basePrices.put("other_kitchen", BigDecimal.ZERO);

        basePrices.put("dust", BigDecimal.ZERO);
        basePrices.put("large_bone", BigDecimal.ZERO);
        basePrices.put("disposable_tableware", BigDecimal.ZERO);
        basePrices.put("other_garbage", BigDecimal.ZERO);
  // 电子产品（需要估价）


        // 试点期上浮价格（前3个月价格上浮10-20%）
        pilotPrices.put("waste_paper", new BigDecimal("1.20"));
        pilotPrices.put("plastic_bottle", new BigDecimal("2.20"));
        pilotPrices.put("old_clothes", new BigDecimal("0.60"));
    }

    // 获取价格方法
    public BigDecimal getPrice(String category, boolean isPilotPeriod) {
        if (isPilotPeriod) {
            return pilotPrices.getOrDefault(category, basePrices.get(category));
        }
        return basePrices.getOrDefault(category, BigDecimal.ZERO);
    }

    // Getter和Setter方法
    public double getPilotPremiumRate() {
        return pilotPremiumRate;
    }
    public void setPilotPremiumRate(double pilotPremiumRate) {
        this.pilotPremiumRate = pilotPremiumRate;
    }
    public Map<String, BigDecimal> getBasePrices() {
        return basePrices;
    }
    public void setBasePrices(Map<String, BigDecimal> basePrices) {
        this.basePrices = basePrices;
    }
    public Map<String, BigDecimal> getPilotPrices() {
        return pilotPrices;
    }
    public void setPilotPrices(Map<String, BigDecimal> pilotPrices) {
        this.pilotPrices = pilotPrices;
    }
}

