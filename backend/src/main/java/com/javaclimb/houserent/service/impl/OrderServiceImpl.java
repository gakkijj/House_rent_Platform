package com.javaclimb.houserent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaclimb.houserent.common.enums.OrderStatusEnum;
import com.javaclimb.houserent.entity.Order;
import com.javaclimb.houserent.mapper.OrderMapper;
import com.javaclimb.houserent.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderMapper orderMapper;
    @Override
    public BaseMapper<Order> getRepository() {
        return orderMapper;
    }

    @Override
    public QueryWrapper<Order> getQueryWrapper(Order order) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (order!=null){
            if(order.getCustomerUserId()!=null){
                queryWrapper.eq("customer_user_id",order.getCustomerUserId());
            }
            if (order.getOwnerUserId()!=null){
                queryWrapper.eq("owner_user_id",order.getOwnerUserId());
            }
        }
        return queryWrapper;
    }

    @Override
    public QueryWrapper<Order> getQueryWrapper(Map<String, Object> condition) {
        return null;
    }

    @Override
    public Order getCurrentEffectiveOrder(Long houseId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("house_id",houseId);
        queryWrapper.eq("status", OrderStatusEnum.NORMAL);
        return orderMapper.selectOne(queryWrapper);
    }
    @Override
    public List<Order> findOverDueOrderList() {
        return orderMapper.findOverDueOrderList();
    }

    @Override
    public String orderNum() {
        return orderMapper.orderNum();
    }
}
