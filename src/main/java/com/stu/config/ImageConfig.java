package com.stu.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Component
public class ImageConfig {

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Value("${app.images.category-path}")
    private String categoryImagePath;

    @Value("${app.images.identity-bg-path}")
    private String identityBgPath;

    /**
     * 获取分类图片URL（修正后的路径）
     */
    public String getCategoryImageUrl(Long categoryId, String level) {
        return contextPath + "/images/category/" + level + "/" + categoryId + ".png";
    }

    /**
     * 获取身份背景图URL
     */
    public String getIdentityImageUrl(Integer identityType) {
        return contextPath + "/identity/proxy/" + identityType;
    }
    

    /**
     * 获取身份背景图存储目录
     */
    public String getIdentityBgBasePath() {
        return identityBgPath;
    }

    /**
     * 初始化目录
     */
    @PostConstruct
    public void init() {
        createDirectory(categoryImagePath + "/level1");
        createDirectory(categoryImagePath + "/level2");
        createDirectory(identityBgPath);

        System.out.println("图片目录初始化完成:");
        System.out.println("分类图片目录: " + new File(categoryImagePath).getAbsolutePath());
        System.out.println("身份背景图目录: " + new File(identityBgPath).getAbsolutePath());
        System.out.println("Context Path: " + contextPath);
    }

    private void createDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("创建目录 " + path + ": " + (created ? "成功" : "失败"));
        }
    }
}