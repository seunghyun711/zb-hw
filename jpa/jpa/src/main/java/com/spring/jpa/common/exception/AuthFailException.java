package com.spring.jpa.common.exception;

public class AuthFailException extends RuntimeException {
    public AuthFailException(String string) {
        super(string);
    }
}
