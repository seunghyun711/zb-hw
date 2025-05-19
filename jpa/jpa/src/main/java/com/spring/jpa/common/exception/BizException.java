package com.spring.jpa.common.exception;

public class BizException extends RuntimeException {
    public BizException(String message) {
        super(message);
    }
}
