package com.javaclimb.houserent.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.javaclimb.houserent.common.constant.Constant;
import com.javaclimb.houserent.common.dto.JsonResult;
import com.javaclimb.houserent.common.enums.HouseStatusEnum;
import com.javaclimb.houserent.common.enums.OrderStatusEnum;
import com.javaclimb.houserent.common.util.DateUtil;
import com.javaclimb.houserent.entity.House;
import com.javaclimb.houserent.entity.Order;
import com.javaclimb.houserent.entity.User;
import com.javaclimb.houserent.service.HouseService;
import com.javaclimb.houserent.service.OrderService;
import com.javaclimb.houserent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** 租赁订单的 Vue 接口，沿用旧项目的“合同后付款”状态机。 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController extends ApiControllerSupport {
    @Autowired private OrderService orderService;
    @Autowired private HouseService houseService;
    @Autowired private UserService userService;

    @GetMapping("/mine")
    public JsonResult mine() {
        User user = getLoginUser();
        if (user == null) return loginRequired();
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.and(query -> query.eq("customer_user_id", user.getId()).or().eq("owner_user_id", user.getId()));
        wrapper.orderByDesc("create_time");
        List<Map<String, Object>> items = new ArrayList<>();
        for (Order order : orderService.getRepository().selectList(wrapper)) items.add(orderView(order, userService, houseService));
        return JsonResult.success("操作成功", items);
    }

    @GetMapping("/{id}")
    public JsonResult detail(@PathVariable Long id) {
        Order order = orderService.get(id);
        if (order == null) return JsonResult.error("订单不存在");
        if (!canAccess(order)) return JsonResult.error("没有权限查看该订单");
        return JsonResult.success("操作成功", orderView(order, userService, houseService));
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public JsonResult create(@RequestBody Map<String, Object> body) {
        User customer = getLoginUser();
        if (customer == null) return loginRequired();
        Long houseId = longValue(body.get("houseId"));
        if (houseId == null) return JsonResult.error("请选择房源");
        House house = houseService.get(houseId);
        if (house == null) return JsonResult.error("房源不存在");
        if (!Objects.equals(house.getStatus(), HouseStatusEnum.NOT_RENT.getValue()) || orderService.getCurrentEffectiveOrder(houseId) != null) {
            return JsonResult.error("房源当前不可租");
        }
        Date endDate = dateValue(body.get("endDate"));
        if (endDate == null) return JsonResult.error("退租日期格式不正确");
        Date startDate = new Date();
        Integer dayNum = DateUtil.daysBetween(startDate, endDate);
        if (dayNum < Constant.MIN_RENT_DAYS) return JsonResult.error("最少租住" + Constant.MIN_RENT_DAYS + "天");
        User owner = userService.get(house.getUserId());
        if (owner == null) return JsonResult.error("房东用户不存在");

        Order order = new Order();
        order.setCreateTime(startDate);
        order.setCustomerUserId(customer.getId());
        order.setOwnerUserId(house.getUserId());
        order.setHouseId(houseId);
        order.setStatus(OrderStatusEnum.NOT_AGREEMENT.getValue());
        order.setMonthRent(house.getMonthRent());
        order.setDayNum(dayNum);
        order.setTotalAmount(house.getMonthRent() * dayNum / 30);
        order.setStartDate(startDate);
        order.setEndDate(endDate);
        orderService.insert(order);
        return JsonResult.success("租赁申请已创建，请确认合同", orderView(order, userService, houseService));
    }

    @PostMapping("/{id}/agreement")
    public JsonResult agreement(@PathVariable Long id) {
        Order order = orderService.get(id);
        if (order == null) return JsonResult.error("订单不存在");
        if (!canAccess(order)) return JsonResult.error("没有权限操作该订单");
        if (!Objects.equals(order.getStatus(), OrderStatusEnum.NOT_AGREEMENT.getValue())) return JsonResult.error("当前订单不能签订合同");
        order.setStatus(OrderStatusEnum.NOT_PAY.getValue());
        orderService.update(order);
        return JsonResult.success("合同已确认，请完成支付", orderView(order, userService, houseService));
    }

    @PostMapping("/{id}/pay")
    @Transactional(rollbackFor = Exception.class)
    public JsonResult pay(@PathVariable Long id) {
        Order order = orderService.get(id);
        if (order == null) return JsonResult.error("订单不存在");
        if (!canAccess(order)) return JsonResult.error("没有权限操作该订单");
        if (!Objects.equals(order.getStatus(), OrderStatusEnum.NOT_PAY.getValue())) return JsonResult.error("当前订单不能支付");
        House house = houseService.get(order.getHouseId());
        if (house == null || !Objects.equals(house.getStatus(), HouseStatusEnum.NOT_RENT.getValue()) || orderService.getCurrentEffectiveOrder(order.getHouseId()) != null) {
            return JsonResult.error("房源当前不可支付");
        }
        order.setStatus(OrderStatusEnum.NORMAL.getValue());
        orderService.update(order);
        house.setStatus(HouseStatusEnum.HAS_RENT.getValue());
        house.setLastOrderStartTime(order.getStartDate());
        house.setLastOrderEndTime(order.getEndDate());
        houseService.update(house);
        return JsonResult.success("支付成功，请联系房东入住", orderView(order, userService, houseService));
    }

    private boolean canAccess(Order order) {
        return getLoginUser() != null && (loginUserIsAdmin() || Objects.equals(getLoginUserId(), order.getCustomerUserId()) || Objects.equals(getLoginUserId(), order.getOwnerUserId()));
    }

    private Long longValue(Object value) {
        if (value == null) return null;
        try { return Long.valueOf(String.valueOf(value)); } catch (NumberFormatException ignored) { return null; }
    }

    private Date dateValue(Object value) {
        if (value == null) return null;
        try { return new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(value)); } catch (ParseException ignored) { return null; }
    }
}
