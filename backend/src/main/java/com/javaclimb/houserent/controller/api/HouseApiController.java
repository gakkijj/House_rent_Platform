package com.javaclimb.houserent.controller.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.javaclimb.houserent.common.dto.JsonResult;
import com.javaclimb.houserent.common.util.PageUtil;
import com.javaclimb.houserent.common.vo.HouseSearchVO;
import com.javaclimb.houserent.entity.House;
import com.javaclimb.houserent.entity.Mark;
import com.javaclimb.houserent.entity.User;
import com.javaclimb.houserent.service.HouseService;
import com.javaclimb.houserent.service.MarkService;
import com.javaclimb.houserent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/houses")
public class HouseApiController extends ApiControllerSupport {
    @Autowired
    private HouseService houseService;
    @Autowired
    private MarkService markService;
    @Autowired
    private UserService userService;

    @GetMapping
    public JsonResult list(@RequestParam(value = "keyword", required = false) String keyword,
                           @RequestParam(value = "city", required = false) String city,
                           @RequestParam(value = "rentType", required = false) String rentType,
                           @RequestParam(value = "minRent", required = false) Integer minRent,
                           @RequestParam(value = "maxRent", required = false) Integer maxRent,
                           @RequestParam(value = "page", defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "size", defaultValue = "24") Integer pageSize) {
        HouseSearchVO search = new HouseSearchVO();
        search.setAddress(keyword == null ? "" : keyword.trim());
        search.setCity(city == null ? "" : city.trim());
        search.setRentType(rentType == null ? "" : rentType.trim());
        search.setPriceRange((minRent == null ? 0 : Math.max(0, minRent)) + ";" + (maxRent == null ? 20000 : maxRent));
        search.setPage(pageNumber);
        search.setSize(Math.min(Math.max(pageSize, 1), 100));
        Page<House> page = PageUtil.initMpPage(search.getPage(), search.getSize());
        Page<House> result = houseService.getHousePage(search, page);
        List<Map<String, Object>> items = new ArrayList<>();
        for (House house : result.getRecords()) items.add(houseView(house, userService));
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("items", items);
        payload.put("total", result.getTotal());
        payload.put("page", result.getCurrent());
        payload.put("size", result.getSize());
        return JsonResult.success("操作成功", payload);
    }

    @GetMapping("/{id}")
    public JsonResult detail(@PathVariable Long id) {
        House house = houseService.get(id);
        return house == null ? JsonResult.error("房源不存在") : JsonResult.success("操作成功", houseView(house, userService));
    }

    @GetMapping("/favorites")
    public JsonResult favorites() {
        User user = getLoginUser();
        if (user == null) return loginRequired();
        QueryWrapper<Mark> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", user.getId()).orderByDesc("create_time");
        List<Mark> marks = markService.getRepository().selectList(wrapper);
        List<Map<String, Object>> items = new ArrayList<>();
        for (Mark mark : marks) {
            House house = houseService.get(mark.getHouseId());
            if (house != null) items.add(houseView(house, userService));
        }
        return JsonResult.success("操作成功", items);
    }

    @PostMapping("/{id}/favorite")
    public JsonResult favorite(@PathVariable Long id) {
        User user = getLoginUser();
        if (user == null) return loginRequired();
        if (houseService.get(id) == null) return JsonResult.error("房源不存在");
        List<Mark> exists = markService.findByUserIdAndHouseId(user.getId(), id);
        if (exists != null && !exists.isEmpty()) return JsonResult.success("已收藏", true);
        Mark mark = new Mark();
        mark.setUserId(user.getId());
        mark.setHouseId(id);
        mark.setCreateTime(new Date());
        markService.insert(mark);
        return JsonResult.success("收藏成功", true);
    }

    @DeleteMapping("/{id}/favorite")
    public JsonResult unfavorite(@PathVariable Long id) {
        User user = getLoginUser();
        if (user == null) return loginRequired();
        List<Mark> marks = markService.findByUserIdAndHouseId(user.getId(), id);
        for (Mark mark : marks) markService.delete(mark.getId());
        return JsonResult.success("已取消收藏", false);
    }
}
