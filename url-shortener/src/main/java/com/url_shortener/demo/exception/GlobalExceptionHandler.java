package com.url_shortener.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUrlException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidUrl(InvalidUrlException exception){
        Map<String,Object> response = Map.of(
                "status", 400,
                "message", exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ShortCodeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleShortCodeNotFound(ShortCodeNotFoundException exception){
        Map<String,Object> response = Map.of(
                "status", 404,
                "message", exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
