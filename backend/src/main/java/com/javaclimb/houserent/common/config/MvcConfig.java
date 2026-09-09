package com.javaclimb.houserent.common.config;

import com.javaclimb.houserent.common.constant.Constant;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * HTTP 资源配置。
 *
 * Vue 页面由独立的 Nginx 容器托管；后端只对外提供 REST API 与上传文件资源。
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:///"+Constant.UPLOADS_PATH);
    }
}
