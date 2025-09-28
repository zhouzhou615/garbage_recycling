package com.stu.controller;

import com.stu.service.ImageService;
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
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/identity")
@Tag(name = "身份背景图管理", description = "身份类型背景图获取相关接口")
public class IdentityImageController {

    private static final Logger logger = LoggerFactory.getLogger(IdentityImageController.class);

    @Autowired
    private ImageService imageService;

    // 身份类型映射
    private final Map<Integer, String> identityNameMap = Map.of(
            1, "个人/其他",
            2, "高校教职工",
            3, "学生",
            4, "职场人"
    );

    @GetMapping("/all")
    @Operation(summary = "获取所有身份类型背景图信息")
    public Result getAllIdentityBackgrounds() {
        try {
            logger.info("开始获取所有身份类型背景图信息");

            List<Map<String, Object>> identityList = new ArrayList<>();
            for (Integer identityType : identityNameMap.keySet()) {
                Map<String, Object> identityInfo = new HashMap<>();
                identityInfo.put("identityType", identityType);
                identityInfo.put("displayName", identityNameMap.get(identityType));
                identityInfo.put("imageUrl", imageService.getIdentityImageUrl(identityType));
                identityList.add(identityInfo);
            }

            identityList.sort(Comparator.comparing(o -> (Integer) o.get("identityType")));
            logger.info("成功获取{}条身份类型背景图信息", identityList.size());

            return Result.success(Map.of(
                    "identities", identityList,
                    "total", identityList.size()
            ));

        } catch (Exception e) {
            logger.error("获取身份背景图信息失败", e);
            return Result.error("获取身份背景图信息失败");
        }
    }

    @GetMapping("/proxy/{identityType}")
    @Operation(summary = "获取身份类型背景图（代理接口）")
    public ResponseEntity<byte[]> getIdentityImageProxy(
            @Parameter(description = "身份类型标识", required = true)
            @PathVariable Integer identityType) {
        try {
            logger.info("处理身份类型[{}]的图片代理请求", identityType);

            String imagePath = imageService.getIdentityImagePath(identityType);
            Path path = Paths.get(imagePath).normalize();

            if (!Files.exists(path)) {
                logger.warn("身份类型[{}]的图片文件不存在: {}", identityType, path);
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(path);
            MediaType mediaType = getMediaType(imagePath);
            logger.info("成功加载身份类型[{}]的图片，大小: {}字节", identityType, imageBytes.length);

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CACHE_CONTROL, "public, max-age=3600")
                    .body(imageBytes);

        } catch (Exception e) {
            logger.error("加载身份类型[{}]的图片失败", identityType, e);
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType getMediaType(String filename) {
        String lowerCaseFilename = filename.toLowerCase();
        if (lowerCaseFilename.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (lowerCaseFilename.endsWith(".jpg") || lowerCaseFilename.endsWith(".jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (lowerCaseFilename.endsWith(".gif")) {
            return MediaType.IMAGE_GIF;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}