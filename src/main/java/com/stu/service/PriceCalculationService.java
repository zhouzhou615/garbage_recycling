package com.stu.service;

import com.stu.entity.DormitoryOrderDTO;
import com.stu.entity.TextbookOrderDTO;

import java.math.BigDecimal;

public interface PriceCalculationService {
    // 计算教材回收金额
    /**
     * 计算教材回收金额
     * @param orderDTO 教材订单数据
     * @return 计算出的金额
     */
    BigDecimal calculateTextbookAmount(TextbookOrderDTO orderDTO);

    // 计算宿舍批量回收金额
    /**
     * 计算宿舍批量回收金额
     * @param orderDTO 宿舍订单数据
     * @return 计算出的金额
     */
    BigDecimal calculateDormitoryAmount(DormitoryOrderDTO orderDTO);
}
