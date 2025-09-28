package com.stu.service;

import com.stu.entity.Category;
import java.util.List;

public interface CategoryService {

    /**
     * 获取所有一级分类
     */
    List<Category> getTopCategories();

    /**
     * 根据父分类ID获取子分类
     */
    List<Category> getSubCategoriesByParentId(Long parentId);

    /**
     * 根据ID获取分类
     */
    Category getCategoryById(Long id);

    /**
     * 验证分类是否存在且为一级分类
     */
    boolean isValidTopCategory(Long categoryId);
}