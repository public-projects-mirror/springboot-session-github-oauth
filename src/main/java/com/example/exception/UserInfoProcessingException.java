package com.example.exception;

import org.springframework.http.HttpStatus;

public class UserInfoProcessingException extends RuntimeException implements BaseHttpException{
    HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public UserInfoProcessingException(String message) {
        super(message);
    }

    public UserInfoProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
