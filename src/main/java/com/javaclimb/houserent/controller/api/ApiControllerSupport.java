package com.javaclimb.houserent.controller.api;

import com.alibaba.fastjson.JSON;
import com.javaclimb.houserent.common.base.BaseController;
import com.javaclimb.houserent.common.dto.JsonResult;
import com.javaclimb.houserent.entity.House;
import com.javaclimb.houserent.entity.Order;
import com.javaclimb.houserent.entity.User;
import com.javaclimb.houserent.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Vue API 的共享序列化与权限辅助方法。
 *
 * 保留数据库实体给持久层使用，不把密码、内部状态等实体直接暴露给浏览器。
 */
abstract class ApiControllerSupport extends BaseController {
    protected JsonResult loginRequired() {
        return JsonResult.error("请先登录");
    }

    protected JsonResult adminRequired() {
        return JsonResult.error("没有管理员权限");
    }

    protected Map<String, Object> userView(User user) {
        if (user == null) {
            return null;
        }
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", user.getId());
        view.put("userName", user.getUserName());
        view.put("name", emptyTo(user.getUserDisplayName(), user.getUserName()));
        view.put("phone", user.getPhone());
        view.put("email", user.getEmail());
        view.put("role", user.getRole());
        view.put("status", user.getStatus());
        view.put("avatar", user.getUserAvatar());
        view.put("description", user.getUserDesc());
        return view;
    }

    protected Map<String, Object> houseView(House house, UserService userService) {
        Map<String, Object> view = new LinkedHashMap<>();
        view.put("id", house.getId());
        view.put("title", house.getTitle());
        view.put("city", house.getCity());
        // 旧表没有 district 字段；先以完整地址呈现，后续表结构升级时再拆分。
        view.put("district", house.getAddress());
        view.put("address", house.getAddress());
        view.put("rentType", house.getRentType());
        view.put("monthRent", house.getMonthRent());
        view.put("area", house.getArea());
        view.put("bedroomNum", house.getBedroomNum());
        view.put("toiletNum", house.getToiletNum());
        view.put("kitchenNum", house.getKitchenNum());
        view.put("livingRoomNum", house.getLivingRoomNum());
        view.put("floor", house.getFloor());
        view.put("maxFloor", house.getMaxFloor());
        view.put("direction", house.getDirection());
        view.put("buildYear", house.getBuildYear());
        view.put("hasElevator", integerToBoolean(house.getHasElevator()));
        view.put("hasAirConditioner", integerToBoolean(house.getHasAirConditioner()));
        view.put("status", houseStatus(house.getStatus()));
        view.put("statusCode", house.getStatus());
        view.put("description", house.getContent());
        view.put("thumbnailUrl", house.getThumbnailUrl());
        view.put("cover", coverFor(house));
        view.put("longitudeLatitude", house.getLongitudeLatitude());
        view.put("contactName", house.getContactName());
        view.put("contactPhone", house.getContactPhone());
        view.put("createdAt", house.getCreateTime());
        view.put("tags", tagsFor(house));
        view.put("slideImages", slideImages(house.getSlideUrl()));
        view.put("landlord", userView(userService.get(house.getUserId())));
        return view;
    }

    protected Map<String, Object> orderView(Order order, UserService userService, com.javaclimb.houserent.service.HouseService houseService) {
        Map<String, Object> view = new LinkedHashMap<>();
        House house = houseService.get(order.getHouseId());
        User customer = userService.get(order.getCustomerUserId());
        view.put("id", order.getId());
        view.put("houseId", order.getHouseId());
        view.put("houseTitle", house == null ? "房源已删除" : house.getTitle());
        view.put("customerName", customer == null ? "未知租客" : emptyTo(customer.getUserDisplayName(), customer.getUserName()));
        view.put("customerPhone", customer == null ? "" : customer.getPhone());
        view.put("monthRent", order.getMonthRent());
        view.put("dayNum", order.getDayNum());
        view.put("totalAmount", order.getTotalAmount());
        view.put("startDate", order.getStartDate());
        view.put("endDate", order.getEndDate());
        view.put("status", orderStatus(order.getStatus()));
        view.put("statusCode", order.getStatus());
        view.put("createdAt", order.getCreateTime());
        return view;
    }

    protected String emptyTo(String value, String fallback) {
        return value == null || value.trim().isEmpty() ? fallback : value;
    }

    private Boolean integerToBoolean(Integer value) {
        return value != null && value == 1;
    }

    private String houseStatus(Integer value) {
        if (value == null || value == 0) return "available";
        if (value == 1) return "rented";
        if (value == -1) return "down";
        if (value == -2) return "pending";
        return "rejected";
    }

    private String orderStatus(Integer value) {
        if (value == null || value == -2) return "pending_agreement";
        if (value == -1) return "pending_payment";
        if (value == 0) return "active";
        if (value == 1) return "expired";
        if (value == -3) return "cancelled";
        if (value == 2) return "ending";
        return "end_rejected";
    }

    private List<String> slideImages(String slideUrl) {
        if (slideUrl == null || slideUrl.trim().isEmpty()) return Collections.emptyList();
        try {
            return JSON.parseArray(slideUrl, String.class);
        } catch (Exception ignored) {
            return Collections.emptyList();
        }
    }

    private List<String> tagsFor(House house) {
        List<String> tags = new ArrayList<>();
        tags.add("whole".equals(house.getRentType()) ? "整租" : "合租");
        if (house.getHasElevator() != null && house.getHasElevator() == 1) tags.add("有电梯");
        if (house.getHasAirConditioner() != null && house.getHasAirConditioner() == 1) tags.add("有空调");
        if (house.getDirection() != null && !house.getDirection().trim().isEmpty()) tags.add(house.getDirection() + "向");
        return tags;
    }

    private String coverFor(House house) {
        if (house.getThumbnailUrl() != null && !house.getThumbnailUrl().trim().isEmpty()) {
            return "url('" + house.getThumbnailUrl().replace("'", "%27") + "') center/cover";
        }
        String[] colors = {"#94b7b0, #345a67", "#deaa67, #925d4c", "#889cc5, #46527f", "#9ab9a0, #557564"};
        int index = house.getId() == null ? 0 : (int) (house.getId() % colors.length);
        return "linear-gradient(135deg, " + colors[index] + ")";
    }
}
