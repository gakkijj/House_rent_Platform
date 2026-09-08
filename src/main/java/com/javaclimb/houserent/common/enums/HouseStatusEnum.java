package com.javaclimb.houserent.common.enums;

/**
 * 房子状态枚举
 * 状态：0未出租 1已出租 -1已下架 -2待审核 -3审核不通过
 */
public enum HouseStatusEnum {
    /*未租出*/
    NOT_RENT(0),

    /*已租出*/
    HAS_RENT(1),

    /*-1已下架*/
    HAS_DOWN(-1),

    /*-2待审核*/
    NOT_CHECK(-2),

    /*-3审核不通过*/
    CHECK_REJECT(-3)

    ;

    private Integer value;

    HouseStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
