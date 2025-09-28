package com.stu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.stu.entity.Category;
import com.stu.mapper.CategoryMapper;
import com.stu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> getTopCategories() {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("parent_id");
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort");
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public List<Category> getSubCategoriesByParentId(Long parentId) {
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByAsc("sort");
        return categoryMapper.selectList(queryWrapper);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public boolean isValidTopCategory(Long categoryId) {
        if (categoryId == null) {
            return false;
        }
        Category category = categoryMapper.selectById(categoryId);
        return category != null && category.getParentId() == null && category.getStatus() == 1;
    }
}