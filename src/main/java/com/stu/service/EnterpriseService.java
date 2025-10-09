package com.stu.service;

import com.stu.entity.EnterpriseUser;

public interface EnterpriseService {
    EnterpriseUser validateEnterpriseQualification(String token);
}

