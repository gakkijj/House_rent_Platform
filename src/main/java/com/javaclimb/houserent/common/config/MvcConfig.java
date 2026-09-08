package com.javaclimb.houserent.common.config;

import com.javaclimb.houserent.common.constant.Constant;
import com.javaclimb.houserent.common.interceptor.AdminInterceptor;
import com.javaclimb.houserent.common.interceptor.CustomerInterceptor;
import com.javaclimb.houserent.common.interceptor.OwnerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 前端配置类
 */
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 配置静态资源访问路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:///"+Constant.UPLOADS_PATH);
    }

    /**
     * 配置拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new CustomerInterceptor())
                .addPathPatterns("/legacy/admin/profile")
                .addPathPatterns("/legacy/admin/order")
                .addPathPatterns("/legacy/admin/mark")
                .addPathPatterns("/legacy/admin/home")
                .addPathPatterns("/legacy/admin/feedback")
                .addPathPatterns("/legacy/admin/password");
        registry.addInterceptor(new OwnerInterceptor())
                .addPathPatterns("/legacy/admin/house");
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/legacy/admin/news")
                .addPathPatterns("/legacy/admin/user");

    }
}
