package com.javaclimb.houserent.controller.api;

import com.javaclimb.houserent.common.constant.Constant;
import com.javaclimb.houserent.common.dto.JsonResult;
import com.javaclimb.houserent.common.enums.UserRoleEnum;
import com.javaclimb.houserent.common.enums.UserStatusEnum;
import com.javaclimb.houserent.entity.User;
import com.javaclimb.houserent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/** Session Cookie 同源认证接口，供 Vue 使用。 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthApiController extends ApiControllerSupport {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public JsonResult login(@RequestBody Map<String, String> body, HttpSession session) {
        String userName = body.get("userName");
        String password = body.get("password");
        if (blank(userName) || blank(password)) return JsonResult.error("请输入用户名和密码");
        User user = userService.findByUserName(userName.trim());
        if (user == null) return JsonResult.error("用户不存在");
        if (!password.equals(user.getUserPass())) return JsonResult.error("密码错误");
        if (UserStatusEnum.DISABLE.getValue().equals(user.getStatus())) return JsonResult.error("账户已被禁用，请联系管理员");
        session.setAttribute(Constant.SESSION_USER_KEY, user);
        return JsonResult.success("登录成功", userView(user));
    }

    @PostMapping("/register")
    public JsonResult register(@RequestBody Map<String, String> body, HttpSession session) {
        String userName = body.get("userName");
        String password = body.get("password");
        if (blank(userName) || blank(password)) return JsonResult.error("请输入用户名和密码");
        if (userService.findByUserName(userName.trim()) != null) return JsonResult.error("用户名已存在");

        User user = new User();
        user.setUserName(userName.trim());
        user.setUserDisplayName(emptyTo(body.get("displayName"), userName.trim()));
        user.setUserPass(password);
        user.setPhone(body.get("phone"));
        user.setEmail(body.get("email"));
        user.setRole(UserRoleEnum.CUSTOMER.getValue());
        user.setStatus(UserStatusEnum.ENABLE.getValue());
        user.setIdCard("未填写");
        user.setUserAvatar("/assets/img/default-avatar.jpg");
        user.setUserDesc("这个人很懒，还没有留下介绍。");
        user.setSex("保密");
        user.setHobby("未填写");
        user.setJob("未填写");
        user.setCreateTime(new Date());
        userService.insert(user);
        session.setAttribute(Constant.SESSION_USER_KEY, user);
        return JsonResult.success("注册成功", userView(user));
    }

    @PostMapping("/logout")
    public JsonResult logout(HttpSession session) {
        session.invalidate();
        return JsonResult.success("已退出登录");
    }

    @RequestMapping("/me")
    public JsonResult me() {
        User user = getLoginUser();
        return user == null ? JsonResult.error("未登录") : JsonResult.success("操作成功", userView(user));
    }

    private boolean blank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
