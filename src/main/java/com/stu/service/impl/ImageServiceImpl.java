package com.stu.service.impl;

import com.stu.config.ImageConfig;
import com.stu.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageConfig imageConfig;

    @Override
    public String getCategoryImageUrl(Long categoryId, boolean isTopLevel) {
        String level = isTopLevel ? "level1" : "level2";
        return imageConfig.getCategoryImageUrl(categoryId, level);
    }

    @Override
    public String getIdentityImageUrl(Integer identityType) {
        return imageConfig.getIdentityImageUrl(identityType);
    }

    @Override
    public String getIdentityImagePath(Integer identityType) {
        // 这里需要根据identityType映射到具体的文件名
        String filename = getIdentityImageFilename(identityType);
        String basePath = imageConfig.getIdentityBgBasePath();
        return basePath + File.separator + filename;
    }

    /**
     * 根据身份类型获取对应的图片文件名 - 使用增强型switch
     */
    private String getIdentityImageFilename(Integer identityType) {
        return switch (identityType) {
            case 1 -> "personal.png";
            case 2 -> "faculty-staff.png";
            case 3 -> "student.png";
            case 4 -> "workplace-person.png";
            default -> "personal.png";
        };
    }
}