package com.stu.config;

import com.stu.interceptor.JwtInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("开始配置JWT拦截器...");

        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/wechat/login",
                        "/auth/checkToken",
                        "/identity/all",
                        "/identity/debug/**",
                        "/identity/proxy/**",  // 排除图片代理接口
                        "/test/**",
                        "/debug/**",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/image/static/**",    // 静态资源
                        "/static/**"
                );

        logger.info("JWT拦截器配置完成");
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        logger.info("开始配置静态资源映射...");

        // 处理上传目录
        File baseDir = new File(uploadDir);
        String absolutePath = baseDir.getAbsolutePath();
        if (!absolutePath.endsWith(File.separator)) {
            absolutePath += File.separator;
        }

        // 确保上传目录存在
        if (!baseDir.exists()) {
            boolean created = baseDir.mkdirs();
            if (created) {
                logger.info("上传目录不存在，已自动创建：{}", absolutePath);
            } else {
                logger.error("创建上传目录失败：{}", absolutePath);
            }
        }

        // 处理图片目录
        String imageDir = absolutePath + "identity-bg" + File.separator;
        File imageDirFile = new File(imageDir);
        if (!imageDirFile.exists()) {
            boolean created = imageDirFile.mkdirs();
            if (created) {
                logger.info("图片目录不存在，已自动创建：{}", imageDir);
            } else {
                logger.error("创建图片目录失败：{}", imageDir);
            }
        }

        // 处理Windows路径分隔符
        String fileUrl = "file:" + imageDir.replace("\\", "/");
        logger.info("图片目录映射路径：{}", fileUrl);

        // 静态资源映射配置
        registry.addResourceHandler("/api/image/static/identity-bg/**")
                .addResourceLocations(fileUrl)
                .setCachePeriod(3600);  // 设置缓存时间

        logger.info("静态资源映射配置完成：/api/image/static/identity-bg/** -> {}", fileUrl);
    }
}
