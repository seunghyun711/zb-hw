package com.spring.jpa.user.exception;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException(String string) {
        super(string);

    }
}
