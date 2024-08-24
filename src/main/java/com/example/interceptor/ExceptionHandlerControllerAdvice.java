package com.example.interceptor;

import com.example.exception.BaseHttpException;
import com.example.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<ApiResponse<String>> handleException(Throwable ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(ex.getMessage());
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof BaseHttpException baseHttpException) {
            httpStatus= baseHttpException.getHttpStatus();
            apiResponse.setStatus(httpStatus);
        }
        return ResponseEntity.status(httpStatus).body(apiResponse);
    }
}