package com.spring.jpa.user.exception;

public class ExistsEmailException extends RuntimeException {
    public ExistsEmailException(String string) {
        super(string);

    }
}
