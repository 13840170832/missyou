package com.lin.missyou.exception;

import com.lin.missyou.exception.http.HttpException;

public class CreateSuccess extends HttpException {
    public CreateSuccess(int code) {
        this.httpStatusCode = 201;
        this.code = code;
    }
}
