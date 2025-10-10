package com.stu.service.impl;

import com.stu.entity.EnterpriseUser;
import com.stu.entity.User;
import com.stu.exception.BusinessException;
import com.stu.service.EnterpriseService;
import com.stu.service.UserService;
import com.stu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @Override
    public EnterpriseUser validateEnterpriseQualification(String token) {
        String raw = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
        Long userId = jwtUtil.getUserIdFromToken(raw);
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在或未登录");
        }
        if (user.getUserType() == null || !"enterprise".equalsIgnoreCase(user.getUserType())) {
            throw new BusinessException("仅企业用户可使用该功能，请先完成企业认证");
        }
        EnterpriseUser eu = new EnterpriseUser();
        eu.setId(user.getId());
        eu.setCompanyName(user.getCompanyName());
        eu.setUnifiedSocialCreditCode(user.getUnifiedSocialCreditCode());
        eu.setInvoiceTitle(user.getInvoiceTitle());
        eu.setTaxNumber(user.getTaxNumber());
        return eu;
    }
}
