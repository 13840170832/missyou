package com.lin.missyou.exception;

import com.lin.missyou.exception.http.HttpException;

public class DeleteSuccess extends HttpException {
    public DeleteSuccess(int code) {
        this.httpStatusCode = 200;
        this.code = code;
    }
}
