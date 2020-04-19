package com.lin.missyou.core.enumeration;

import java.util.stream.Stream;

public enum OrderStatus {

    ALL(0,"全部"),
    UNPAID(1,"待支付"),
    PAID(2,"已支付"),
    DELIVERED(3,"已发货"),
    FINISHED(4,"已完成"),
    CANCELED(5,"已取消"),

    //预扣除库存不存在以下两种情况
    PAID_BUT_OUT_OF(21,"已支付,但无货或库存不足"),
    DEAL_OUT_OF(22,"已处理支付但缺货的情况");

    private int value;

    OrderStatus(int value,String text) {
        this.value = value;
    }

    public int value(){
        return this.value;
    }

    public static OrderStatus toType(int value){
        return Stream.of(OrderStatus.values())
                .filter(o -> o.value == value)
                .findAny()
                .orElse(null);
    }
}
