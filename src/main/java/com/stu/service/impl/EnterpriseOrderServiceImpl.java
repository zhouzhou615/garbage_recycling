package com.stu.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.stu.entity.BulkOrderRequest;
import com.stu.entity.EnterpriseOrder;
import com.stu.entity.EnterpriseUser;
import com.stu.mapper.EnterpriseOrderMapper;
import com.stu.service.EnterpriseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.Date;

@Service
public class EnterpriseOrderServiceImpl implements EnterpriseOrderService {

    @Autowired
    private EnterpriseOrderMapper enterpriseOrderMapper;

    @Override
    public EnterpriseOrder createBulkOrder(EnterpriseUser enterprise, BulkOrderRequest request, BigDecimal estimatedAmount) {
        EnterpriseOrder order = new EnterpriseOrder();
        order.setOrderNo(generateOrderNo());
        order.setEnterpriseId(enterprise.getId());
        order.setOrderType("BULK");
        order.setTotalWeight(request.getTotalWeight());
        order.setWeightUnit(request.getWeightUnit().name());
        order.setEstimatedAmount(estimatedAmount);
        order.setInvoiceRequired(Boolean.TRUE.equals(request.getInvoiceRequired()));
        if (request.getInvoiceInfo() != null) {
            order.setInvoiceTitle(request.getInvoiceInfo().getInvoiceTitle());
            order.setTaxNumber(request.getInvoiceInfo().getTaxNumber());
        } else {
            order.setInvoiceTitle(enterprise.getInvoiceTitle());
            order.setTaxNumber(enterprise.getTaxNumber());
        }
        order.setInvoiceStatus("NOT_APPLIED");
        if (request.getScheduleTime() != null) {
            order.setScheduleTime(Date.from(request.getScheduleTime().atZone(ZoneId.systemDefault()).toInstant()));
        }
        order.setStatus("PENDING");
        order.setPickupAddress(request.getPickupAddress());
        order.setContactPerson(request.getContactPerson());
        order.setContactPhone(request.getContactPhone());
        enterpriseOrderMapper.insert(order);
        return order;
    }

    private String generateOrderNo() {
        return "E" + IdWorker.getIdStr();
    }
}

