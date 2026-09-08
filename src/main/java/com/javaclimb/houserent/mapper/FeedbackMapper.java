package com.javaclimb.houserent.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.javaclimb.houserent.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户反馈mapper层
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
}
