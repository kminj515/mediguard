package com.example.mediguard.domain.pharmacy.enums;

import com.example.mediguard.global.code.ResponseCode;
import lombok.Getter;

@Getter
public enum PharmacyErrorCode implements ResponseCode {

    PHARMACY_NOT_FOUND("404", "약국 정보를 찾을 수 없습니다."),
    PHARMACY_ALREADY_EXISTS("409", "이미 등록된 약국입니다."),
    PHARMACY_ACCESS_DENIED("403", "약국 정보에 접근 권한이 없습니다.");

    private final String statusCode;
    private final String message;

    PharmacyErrorCode(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public String getStatusCode() { return statusCode; }

    @Override
    public String getMessage() { return message; }
}