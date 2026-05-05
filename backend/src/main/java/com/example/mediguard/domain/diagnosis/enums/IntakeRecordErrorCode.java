package com.example.mediguard.domain.diagnosis.enums;

import com.example.mediguard.global.code.ResponseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IntakeRecordErrorCode implements ResponseCode {

    INTAKE_RECORD_NOT_FOUND("404", "복약 기록을 찾을 수 없습니다."),
    INTAKE_RECORD_ALREADY_EXISTS("409", "이미 등록된 복약 기록입니다."),
    INTAKE_RECORD_ACCESS_DENIED("403", "복약 기록에 접근 권한이 없습니다.");

    private final String statusCode;
    private final String message;

    @Override
    public String getStatusCode() { return statusCode; }

    @Override
    public String getMessage() { return message; }
}
