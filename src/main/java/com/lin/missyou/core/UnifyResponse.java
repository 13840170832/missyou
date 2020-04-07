package com.lin.missyou.core;

import com.lin.missyou.exception.CreateSuccess;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnifyResponse {

    private int code;
    private String message;
    private String request;

    public UnifyResponse(int code, String message, String request) {
        this.code = code;
        this.message = message;
        this.request = request;
    }

    public static void createSuccess(int code){
        throw new CreateSuccess(code);
    }
}
