package com.leyou.common.advice;

import com.leyou.common.exception.LyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author 45207
 * @create 2018-08-16 14:20
 */
@ControllerAdvice
@Slf4j
public class BasicExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        //日志记录异常
        log.error(e.getMessage(), e);
        //判断是哪种异常
        if (e instanceof LyException) {
            LyException e2 = (LyException) e;
            int code = e2.getStatus() == null ? e2.getStatusCode() : e2.getStatus().value();
            return ResponseEntity.status(code).body(e2.getMessage());
        }
        //否则就是服务器异常,500,但是不能暴露给别人
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("未知错误");
    }
}
