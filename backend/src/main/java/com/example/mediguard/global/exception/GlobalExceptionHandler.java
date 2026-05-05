package com.example.mediguard.global.exception;

import com.example.mediguard.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.example.mediguard.domain")
public class GlobalExceptionHandler {

    // CustomException 하위 모든 예외 한 번에 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(
            CustomException e, HttpServletRequest request) {
        return ResponseEntity
                .status(Integer.parseInt(e.getResponseCode().getStatusCode()))
                .body(ApiResponse.error(e.getResponseCode()));
    }

    // 예상치 못한 서버 에러 (최후 방어선)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(
            Exception e, HttpServletRequest request) {
        System.err.println("======= 예외 발생 =======");
        System.err.println("URI: " + request.getRequestURI());
        System.err.println("메시지: " + e.getMessage());
        System.err.println("클래스: " + e.getClass().getName());
        e.printStackTrace();
        System.err.println("========================");

        return ResponseEntity
                .status(500)
                .body(ApiResponse.error());
    }
}