package com.leyou.common.exception;

import com.leyou.common.enumeration.ExceptionEnumeration;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LyException extends RuntimeException{

    /*响应状态对象*/
    private HttpStatus status;

    /* 响应状态码*/
    private int statusCode;

    public LyException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public LyException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public LyException(ExceptionEnumeration fileExceptionEnumeration){
        super(fileExceptionEnumeration.getMsg());
        this.statusCode = fileExceptionEnumeration.getCode();
    }
}