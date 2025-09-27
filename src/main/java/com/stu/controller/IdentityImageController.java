package com.stu.controller;

import com.stu.util.ImagePathUtil;
import com.stu.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/identity")
@Tag(name = "身份图片接口", description = "身份类型背景图获取及代理相关接口")
public class IdentityImageController {

    private static final Logger logger = LoggerFactory.getLogger(IdentityImageController.class);

    @Autowired
    private ImagePathUtil imagePathUtil;

    /**
     * 获取所有身份类型及其背景图URL（核心接口：返回前端可访问的图片路径）
     */
    @GetMapping("/all")
    @Operation(summary = "获取所有身份类型背景图信息", description = "返回所有身份类型的标识、显示名称及对应的背景图访问URL")
    public Result getAllIdentityBackgrounds() {
        try {
            logger.info("开始获取所有身份类型背景图信息");
            Map<Integer, Map<String, String>> identityMappings = imagePathUtil.getAllIdentityMappings();

            List<Map<String, Object>> identityList = new ArrayList<>();
            for (Map.Entry<Integer, Map<String, String>> entry : identityMappings.entrySet()) {
                Map<String, Object> identityInfo = new HashMap<>();
                identityInfo.put("identityType", entry.getKey());
                identityInfo.put("displayName", entry.getValue().get("displayName"));
                identityInfo.put("imageUrl", entry.getValue().get("imageUrl"));
                identityList.add(identityInfo);
            }

            identityList.sort(Comparator.comparing(o -> (Integer) o.get("identityType")));
            logger.info("成功获取{}条身份类型背景图信息", identityList.size());

            return Result.success(Map.of(
                    "identities", identityList,
                    "total", identityList.size()
            ));

        } catch (Exception e) {
            logger.error("获取身份背景图路径失败", e);
            return Result.error("获取身份背景图信息失败：" + e.getMessage());
        }
    }

    /**
     * 图片代理接口（供imageUrl调用，返回图片二进制数据）
     */
    @GetMapping("/proxy/{identityType}")
    @Operation(summary = "获取身份类型背景图（代理接口）", description = "通过身份类型标识获取对应的背景图二进制数据，供前端直接展示图片")
    public ResponseEntity<byte[]> getIdentityImageProxy(
            @Parameter(description = "身份类型标识（用于区分不同身份类型的背景图）", required = true)
            @PathVariable Integer identityType) {
        try {
            logger.info("开始处理身份类型[{}]的图片代理请求", identityType);
            String filename = imagePathUtil.getIdentityImageFilename(identityType);
            Path imagePath = imagePathUtil.getIdentityBgPath(filename);

            if (!Files.exists(imagePath)) {
                logger.warn("身份类型[{}]的图片文件不存在：{}", identityType, imagePath);
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);
            MediaType mediaType = getMediaType(filename);
            logger.info("成功加载身份类型[{}]的图片，大小：{}字节", identityType, imageBytes.length);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                    .body(imageBytes);

        } catch (Exception e) {
            logger.error("加载身份类型[{}]的图片失败", identityType, e);
            return ResponseEntity.notFound().build();
        }
    }

    // 辅助方法：根据文件名判断媒体类型
    private MediaType getMediaType(String filename) {
        String lowerCaseFilename = filename.toLowerCase();
        if (lowerCaseFilename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (lowerCaseFilename.endsWith(".jpg") || lowerCaseFilename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (lowerCaseFilename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            logger.warn("未知图片类型，文件名：{}", filename);
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}
