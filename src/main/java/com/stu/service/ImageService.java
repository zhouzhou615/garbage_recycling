package com.stu.service;

public interface ImageService {

    /**
     * 获取分类图片URL
     */
    String getCategoryImageUrl(Long categoryId, boolean isTopLevel);

    /**
     * 获取身份背景图URL
     */
    String getIdentityImageUrl(Integer identityType);


    /**
     * 获取身份背景图完整路径（用于文件操作）
     */
    String getIdentityImagePath(Integer identityType);
}