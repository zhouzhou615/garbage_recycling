package com.stu.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Component
public class ImagePathUtil {

    @Value("${app.upload-dir:./uploads}")
    private String baseDir;

    // 身份类型与图片文件名映射
    private final Map<Integer, String> identityImageMap = new HashMap<>();
    // 身份类型与显示名映射
    private final Map<Integer, String> identityNameMap = new HashMap<>();

    public ImagePathUtil() {
        // 初始化身份类型配置
        identityImageMap.put(1, "personal.png");
        identityImageMap.put(2, "faculty-staff.png");
        identityImageMap.put(3, "student.png");
        identityImageMap.put(4, "workplace-person.png");

        identityNameMap.put(1, "个人、其他");
        identityNameMap.put(2, "高校教职工");
        identityNameMap.put(3, "学生");
        identityNameMap.put(4, "职场人");
    }

    /**
     * 生成前端可直接访问的图片URL（核心方法）
     */
    public String getIdentityImageUrl(Integer identityType) {
        return "/api/identity/proxy/" + identityType;
    }

    /**
     * 获取图片本地存储路径
     */
    public Path getIdentityBgPath(String filename) {
        return Paths.get(baseDir, "identity-bg", filename).normalize();
    }

    /**
     * 根据身份类型获取图片文件名
     */
    public String getIdentityImageFilename(Integer identityType) {
        return identityImageMap.getOrDefault(identityType, "personal.png");
    }

    /**
     * 获取所有身份类型的映射关系（包含图片URL）
     */
    public Map<Integer, Map<String, String>> getAllIdentityMappings() {
        Map<Integer, Map<String, String>> result = new HashMap<>();

        for (Integer type : identityImageMap.keySet()) {
            Map<String, String> mapping = new HashMap<>();
            mapping.put("displayName", identityNameMap.get(type));
            mapping.put("imageUrl", getIdentityImageUrl(type));
            result.put(type, mapping);
        }
        return result;
    }
}