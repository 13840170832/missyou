package com.lin.missyou.exception.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpException extends RuntimeException{
    protected Integer code;
    protected Integer httpStatusCode = 500;

}
