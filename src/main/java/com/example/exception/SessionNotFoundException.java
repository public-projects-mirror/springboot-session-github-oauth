package com.example.exception;

import org.springframework.http.HttpStatus;

public class SessionNotFoundException extends RuntimeException implements BaseHttpException{
    public SessionNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
