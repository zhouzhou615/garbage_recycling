package com.stu.controller;

import com.stu.entity.Category;
import com.stu.service.CategoryService;
import com.stu.service.ImageService;
import com.stu.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/category")
@Tag(name = "垃圾分类管理", description = "垃圾分类信息获取接口")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/top")
    @Operation(summary = "获取所有一级分类")
    public Result getTopCategories() {
        try {
            logger.info("开始获取一级分类列表");

            List<Category> categories = categoryService.getTopCategories();

            List<Map<String, Object>> result = categories.stream().map(category -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", category.getId());
                item.put("name", category.getName());
                item.put("imageUrl", imageService.getCategoryImageUrl(category.getId(), true));
                item.put("sort", category.getSort());
                item.put("description", category.getDescription());
                item.put("pricePerKg", category.getPricePerKg());
                return item;
            }).collect(Collectors.toList());

            logger.info("成功获取{}个一级分类", result.size());
            // 将列表包装在 Map 中返回
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("list", result);
            responseData.put("total", result.size());

            return Result.success(responseData);

        } catch (Exception e) {
            logger.error("获取一级分类失败", e);
            return Result.error("获取分类失败");
        }
    }


    @GetMapping("/{parentId}/sub")
    @Operation(summary = "根据一级分类ID获取二级分类")
    public Result getSubCategories(
            @Parameter(description = "一级分类ID", required = true)
            @PathVariable Long parentId) {
        try {
            logger.info("开始获取父分类[{}]的子分类", parentId);

            // 验证父分类是否存在且为一级分类
            if (!categoryService.isValidTopCategory(parentId)) {
                logger.warn("父分类[{}]不存在或不是一级分类", parentId);
                return Result.error("分类不存在或不是一级分类");
            }

            Category parentCategory = categoryService.getCategoryById(parentId);
            List<Category> subCategories = categoryService.getSubCategoriesByParentId(parentId);

            Map<String, Object> result = new HashMap<>();
            result.put("parentCategoryId", parentId);
            result.put("parentCategoryName", parentCategory.getName());

            List<Map<String, Object>> items = subCategories.stream().map(category -> {
                Map<String, Object> item = new HashMap<>();
                item.put("id", category.getId());
                item.put("name", category.getName());
                item.put("imageUrl", imageService.getCategoryImageUrl(category.getId(), false));
                item.put("sort", category.getSort());
                item.put("description", category.getDescription());
                item.put("pricePerKg", category.getPricePerKg());
                return item;
            }).collect(Collectors.toList());

            result.put("items", items);
            result.put("total", items.size());

            logger.info("成功获取父分类[{}]的{}个子分类", parentId, items.size());
            return Result.success(result);

        } catch (Exception e) {
            logger.error("获取子分类失败, parentId: {}", parentId, e);
            return Result.error("获取子分类失败");
        }
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "根据ID获取分类详情")
    public Result getCategoryById(
            @Parameter(description = "分类ID", required = true)
            @PathVariable Long categoryId) {
        try {
            logger.info("开始获取分类[{}]的详情", categoryId);

            Category category = categoryService.getCategoryById(categoryId);
            if (category == null) {
                return Result.error("分类不存在");
            }

            boolean isTopLevel = category.getParentId() == null;
            Map<String, Object> result = new HashMap<>();
            result.put("id", category.getId());
            result.put("name", category.getName());
            result.put("imageUrl", imageService.getCategoryImageUrl(category.getId(), isTopLevel));
            result.put("sort", category.getSort());
            result.put("description", category.getDescription());
            result.put("pricePerKg", category.getPricePerKg());
            result.put("status", category.getStatus());
            result.put("isTopLevel", isTopLevel);

            if (!isTopLevel) {
                Category parentCategory = categoryService.getCategoryById(category.getParentId());
                result.put("parentCategoryName", parentCategory != null ? parentCategory.getName() : "未知");
            }

            logger.info("成功获取分类[{}]的详情", categoryId);
            return Result.success(result);

        } catch (Exception e) {
            logger.error("获取分类详情失败, categoryId: {}", categoryId, e);
            return Result.error("获取分类详情失败");
        }
    }
}