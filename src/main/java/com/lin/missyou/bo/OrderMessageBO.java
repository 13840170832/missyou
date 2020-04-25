package com.lin.missyou.bo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderMessageBO {
    private Long uid;
    private Long oid;
    private Long couponId;
    private String message;

    public OrderMessageBO(String message) {
        this.message = message;
        this.parseId(message);
    }

    private void parseId(String message){
        String[] temp = message.split(",");
        this.uid = Long.valueOf(temp[0]);
        this.oid = Long.valueOf(temp[1]);
        this.couponId = Long.valueOf(temp[2]);
    }
}
