package com.javaclimb.houserent.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaclimb.houserent.common.dto.JsonResult;
import com.javaclimb.houserent.common.enums.FeedbackStatusEnum;
import com.javaclimb.houserent.common.enums.HouseStatusEnum;
import com.javaclimb.houserent.entity.Feedback;
import com.javaclimb.houserent.entity.House;
import com.javaclimb.houserent.entity.News;
import com.javaclimb.houserent.entity.Order;
import com.javaclimb.houserent.entity.User;
import com.javaclimb.houserent.service.FeedbackService;
import com.javaclimb.houserent.service.HouseService;
import com.javaclimb.houserent.service.NewsService;
import com.javaclimb.houserent.service.OrderService;
import com.javaclimb.houserent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Vue 管理台接口；所有入口均在 Controller 层明确校验管理员角色。 */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminApiController extends ApiControllerSupport {
    @Autowired private HouseService houseService;
    @Autowired private UserService userService;
    @Autowired private OrderService orderService;
    @Autowired private FeedbackService feedbackService;
    @Autowired private NewsService newsService;

    @GetMapping("/houses")
    public JsonResult houses() {
        if (!loginUserIsAdmin()) return adminRequired();
        QueryWrapper<House> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Map<String, Object>> items = new ArrayList<>();
        for (House house : houseService.getRepository().selectList(wrapper)) items.add(houseView(house, userService));
        return JsonResult.success("操作成功", items);
    }

    @PostMapping("/houses")
    public JsonResult createHouse(@RequestBody Map<String, Object> body) {
        if (!loginUserIsAdmin()) return adminRequired();
        House house = new House();
        applyHouse(body, house);
        if (blank(house.getTitle()) || blank(house.getCity()) || house.getMonthRent() == null) return JsonResult.error("请填写标题、城市和月租金");
        house.setUserId(getLoginUserId());
        house.setStatus(HouseStatusEnum.NOT_CHECK.getValue());
        house.setCreateTime(new Date());
        houseService.insert(house);
        return JsonResult.success("房源已提交审核", houseView(house, userService));
    }

    @PatchMapping("/houses/{id}")
    public JsonResult updateHouse(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        if (!loginUserIsAdmin()) return adminRequired();
        House house = houseService.get(id);
        if (house == null) return JsonResult.error("房源不存在");
        applyHouse(body, house);
        houseService.update(house);
        return JsonResult.success("房源已更新", houseView(house, userService));
    }

    @PostMapping("/houses/{id}/status")
    public JsonResult updateHouseStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (!loginUserIsAdmin()) return adminRequired();
        House house = houseService.get(id);
        if (house == null) return JsonResult.error("房源不存在");
        String status = body.get("status");
        if ("available".equals(status)) house.setStatus(HouseStatusEnum.NOT_RENT.getValue());
        else if ("rented".equals(status)) house.setStatus(HouseStatusEnum.HAS_RENT.getValue());
        else if ("down".equals(status)) house.setStatus(HouseStatusEnum.HAS_DOWN.getValue());
        else if ("rejected".equals(status)) house.setStatus(HouseStatusEnum.CHECK_REJECT.getValue());
        else return JsonResult.error("不支持的房源状态");
        houseService.update(house);
        return JsonResult.success("状态已更新", houseView(house, userService));
    }

    @DeleteMapping("/houses/{id}")
    public JsonResult deleteHouse(@PathVariable Long id) {
        if (!loginUserIsAdmin()) return adminRequired();
        House house = houseService.get(id);
        if (house == null) return JsonResult.error("房源不存在");
        if (Objects.equals(house.getStatus(), HouseStatusEnum.HAS_RENT.getValue())) return JsonResult.error("房源租住中，不能删除");
        houseService.delete(id);
        return JsonResult.success("房源已删除");
    }

    @GetMapping("/orders")
    public JsonResult orders() {
        if (!loginUserIsAdmin()) return adminRequired();
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Map<String, Object>> items = new ArrayList<>();
        for (Order order : orderService.getRepository().selectList(wrapper)) items.add(orderView(order, userService, houseService));
        return JsonResult.success("操作成功", items);
    }

    @GetMapping("/users")
    public JsonResult users() {
        if (!loginUserIsAdmin()) return adminRequired();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Map<String, Object>> items = new ArrayList<>();
        for (User user : userService.getRepository().selectList(wrapper)) items.add(userView(user));
        return JsonResult.success("操作成功", items);
    }

    @GetMapping("/feedbacks")
    public JsonResult feedbacks() {
        if (!loginUserIsAdmin()) return adminRequired();
        QueryWrapper<Feedback> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        List<Map<String, Object>> items = new ArrayList<>();
        for (Feedback feedback : feedbackService.getRepository().selectList(wrapper)) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", feedback.getId());
            item.put("user", feedback.getContactName());
            item.put("content", feedback.getContent());
            item.put("status", Objects.equals(feedback.getStatus(), FeedbackStatusEnum.HAS_HANDLE.getValue()) ? "resolved" : "pending");
            item.put("createdAt", feedback.getCreateTime());
            items.add(item);
        }
        return JsonResult.success("操作成功", items);
    }

    @PostMapping("/feedbacks/{id}/resolve")
    public JsonResult resolveFeedback(@PathVariable Long id) {
        if (!loginUserIsAdmin()) return adminRequired();
        Feedback feedback = feedbackService.get(id);
        if (feedback == null) return JsonResult.error("反馈不存在");
        feedback.setStatus(FeedbackStatusEnum.HAS_HANDLE.getValue());
        feedbackService.update(feedback);
        return JsonResult.success("反馈已标记为已处理");
    }

    @GetMapping("/news")
    public JsonResult adminNews() {
        if (!loginUserIsAdmin()) return adminRequired();
        QueryWrapper<News> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        return JsonResult.success("操作成功", newsService.getRepository().selectList(wrapper));
    }

    private void applyHouse(Map<String, Object> body, House house) {
        if (body.containsKey("title")) house.setTitle(stringValue(body.get("title")));
        if (body.containsKey("city")) house.setCity(stringValue(body.get("city")));
        if (body.containsKey("address")) house.setAddress(stringValue(body.get("address")));
        if (body.containsKey("rentType")) house.setRentType(stringValue(body.get("rentType")));
        if (body.containsKey("description")) house.setContent(stringValue(body.get("description")));
        if (body.containsKey("content")) house.setContent(stringValue(body.get("content")));
        if (body.containsKey("monthRent")) house.setMonthRent(integerValue(body.get("monthRent")));
        if (body.containsKey("area")) house.setArea(doubleValue(body.get("area")));
        if (body.containsKey("bedroomNum")) house.setBedroomNum(integerValue(body.get("bedroomNum")));
        if (body.containsKey("toiletNum")) house.setToiletNum(integerValue(body.get("toiletNum")));
        if (body.containsKey("kitchenNum")) house.setKitchenNum(integerValue(body.get("kitchenNum")));
        if (body.containsKey("livingRoomNum")) house.setLivingRoomNum(integerValue(body.get("livingRoomNum")));
        if (body.containsKey("floor")) house.setFloor(integerValue(body.get("floor")));
        if (body.containsKey("maxFloor")) house.setMaxFloor(integerValue(body.get("maxFloor")));
        if (body.containsKey("direction")) house.setDirection(stringValue(body.get("direction")));
        if (body.containsKey("buildYear")) house.setBuildYear(integerValue(body.get("buildYear")));
        if (body.containsKey("hasElevator")) house.setHasElevator(booleanValue(body.get("hasElevator")) ? 1 : 0);
        if (body.containsKey("hasAirConditioner")) house.setHasAirConditioner(booleanValue(body.get("hasAirConditioner")) ? 1 : 0);
    }

    private String stringValue(Object value) { return value == null ? null : String.valueOf(value).trim(); }
    private Integer integerValue(Object value) { try { return value == null ? null : Integer.valueOf(String.valueOf(value)); } catch (NumberFormatException e) { return null; } }
    private Double doubleValue(Object value) { try { return value == null ? null : Double.valueOf(String.valueOf(value)); } catch (NumberFormatException e) { return null; } }
    private boolean booleanValue(Object value) { return "true".equalsIgnoreCase(String.valueOf(value)) || "1".equals(String.valueOf(value)); }
    private boolean blank(String value) { return value == null || value.trim().isEmpty(); }
}
