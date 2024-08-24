package com.example.exception;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends RuntimeException implements BaseHttpException{
    public UserAlreadyExistsException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.CONFLICT;
    }
}
