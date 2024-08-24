package com.example.exception;

import org.springframework.http.HttpStatus;

public class OAuth2TokenRetrievalException extends RuntimeException implements BaseHttpException {
    HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public OAuth2TokenRetrievalException(String message) {
        super(message);
    }

    public OAuth2TokenRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
