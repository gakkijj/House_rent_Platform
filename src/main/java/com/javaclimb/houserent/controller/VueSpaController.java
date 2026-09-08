package com.javaclimb.houserent.controller;

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
    public String index() {
        return "forward:/index.html";
    }
}
