package com.example.exception;

import org.springframework.http.HttpStatus;

public interface BaseHttpException {
    HttpStatus getHttpStatus();
}
