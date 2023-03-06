package com.haris.oauth2.sample.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", new Date());
        response.put("status", 500);
        response.put("error", e.getMessage());

        return ResponseEntity.internalServerError().body(response);
    }
}
