package com.javaclimb.houserent.common.enums;

/**
 * 反馈状态枚举
 * 处理状态：0待处理 1已处理
 */
public enum FeedbackStatusEnum {
    /*0待处理*/
    NOT_HANDLE(0),

    /*1已处理*/
    HAS_HANDLE(1)


    ;

    private Integer value;

    FeedbackStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
