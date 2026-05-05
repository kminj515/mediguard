package com.example.mediguard.domain.diagnosis.enums;

import com.example.mediguard.global.code.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MedicineErrorCode implements ResponseCode {

    MEDICINE_NOT_FOUND("404", "약 정보를 찾을 수 없습니다."),
    MEDICINE_ALREADY_EXISTS("409", "이미 등록된 약입니다."),
    MEDICINE_ACCESS_DENIED("403", "약 정보에 접근 권한이 없습니다.");

    private final String statusCode;
    private final String message;

    @Override
    public String getStatusCode() { return statusCode; }

    @Override
    public String getMessage() { return message; }
}
