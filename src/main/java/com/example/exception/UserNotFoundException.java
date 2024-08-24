package com.example.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException implements BaseHttpException {
    public UserNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
