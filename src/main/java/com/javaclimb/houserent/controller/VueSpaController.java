package com.javaclimb.houserent.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Vue Router 使用 history 模式时，刷新任意前端路由都应回到同一个入口文件。
 * /api 与上传文件由各自的 Controller/ResourceHandler 处理，不会落到这里。
 */
@Controller
public class VueSpaController {
    @GetMapping({
            "/",
            "/houses", "/houses/**",
            "/favorites", "/orders", "/orders/**",
            "/feedback", "/login", "/register",
            "/news", "/news/**",
            "/admin", "/admin/**"
    })
    public ResponseEntity<Resource> index() {
        /*
         * 这个旧项目启用了 @EnableWebMvc，Spring Boot 默认的视图解析器不会生效。
         * 因此不使用 "forward:/index.html"，而是直接把 Vue 构建入口作为静态 HTML 返回。
         */
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(new ClassPathResource("static/index.html"));
    }
}
