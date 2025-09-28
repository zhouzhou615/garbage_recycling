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

    @Value("${app.images.category-path}")
    private String categoryImagePath;

    @Value("${app.images.identity-bg-path}")
    private String identityBgPath;


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
                        "/identity/proxy/**",
                        "/category/**",           // 分类接口无需认证
                        "/test/**",
                        "/debug/**",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**",
                        "/api/images/**",         // 修正：静态资源路径（包含context-path）
                        "/images/**"              // 保留：兼容性路径
                );

        logger.info("JWT拦截器配置完成");
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        logger.info("开始配置静态资源映射...");

        // 身份背景图静态资源映射
        String identityBgAbsolutePath = new File(identityBgPath).getAbsolutePath();
        String identityBgFileUrl = "file:" + identityBgAbsolutePath.replace("\\", "/") + "/";

        // 使用相对路径，不包含context-path
        registry.addResourceHandler("/images/identity-bg/**")
                .addResourceLocations(identityBgFileUrl)
                .setCachePeriod(3600);
        logger.info("身份背景图映射: /images/identity-bg/** -> {}", identityBgFileUrl);

        // 分类图片静态资源映射
        String categoryImageAbsolutePath = new File(categoryImagePath).getAbsolutePath();
        String categoryImageFileUrl = "file:" + categoryImageAbsolutePath.replace("\\", "/") + "/";

        registry.addResourceHandler("/images/category/**")
                .addResourceLocations(categoryImageFileUrl)
                .setCachePeriod(3600);
        logger.info("分类图片映射: /images/category/** -> {}", categoryImageFileUrl);

        logger.info("静态资源映射配置完成");
    }
}