package com.example.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException implements BaseHttpException {
    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.FORBIDDEN;
    }
}
