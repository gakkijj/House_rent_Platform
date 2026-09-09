package com.javaclimb.houserent.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaclimb.houserent.common.dto.JsonResult;
import com.javaclimb.houserent.common.enums.FeedbackStatusEnum;
import com.javaclimb.houserent.entity.Feedback;
import com.javaclimb.houserent.entity.News;
import com.javaclimb.houserent.entity.User;
import com.javaclimb.houserent.service.FeedbackService;
import com.javaclimb.houserent.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ContentApiController extends ApiControllerSupport {
    @Autowired private NewsService newsService;
    @Autowired private FeedbackService feedbackService;

    @GetMapping("/news")
    public JsonResult news() {
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return JsonResult.success("操作成功", newsService.getRepository().selectList(wrapper));
    }

    @GetMapping("/news/{id}")
    public JsonResult newsDetail(@PathVariable Long id) {
        News news = newsService.get(id);
        return news == null ? JsonResult.error("资讯不存在") : JsonResult.success("操作成功", news);
    }

    @PostMapping("/feedbacks")
    public JsonResult createFeedback(@RequestBody Map<String, String> body) {
        User user = getLoginUser();
        if (user == null) return loginRequired();
        String content = body.get("content");
        if (content == null || content.trim().isEmpty()) return JsonResult.error("请输入反馈内容");
        Feedback feedback = new Feedback();
        feedback.setTitle(emptyTo(body.get("title"), "用户反馈"));
        feedback.setContent(content.trim());
        feedback.setUserId(user.getId());
        feedback.setContactName(emptyTo(body.get("contactName"), emptyTo(user.getUserDisplayName(), user.getUserName())));
        feedback.setContactEmail(emptyTo(body.get("contactEmail"), user.getEmail()));
        feedback.setStatus(FeedbackStatusEnum.NOT_HANDLE.getValue());
        feedback.setCreateTime(new Date());
        feedbackService.insert(feedback);
        return JsonResult.success("反馈已提交，感谢你的建议", feedback.getId());
    }
}
