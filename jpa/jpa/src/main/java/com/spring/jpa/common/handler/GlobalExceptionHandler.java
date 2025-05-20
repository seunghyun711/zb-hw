package com.spring.jpa.common.handler;

import com.spring.jpa.common.exception.AuthFailException;
import com.spring.jpa.common.model.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthFailException.class)
    public ResponseEntity<?> authFailException(AuthFailException e) {
        
        return ResponseResult.fail("[인증 실패]" + e.getMessage());
    }
}
