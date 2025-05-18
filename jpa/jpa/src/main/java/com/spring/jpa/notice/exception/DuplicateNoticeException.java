package com.spring.jpa.notice.exception;

public class DuplicateNoticeException extends RuntimeException {
    public DuplicateNoticeException(String message) {
        super(message);
    }
}
