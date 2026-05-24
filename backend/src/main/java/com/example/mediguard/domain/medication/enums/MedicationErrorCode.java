package com.example.mediguard.domain.medication.enums;

import com.example.mediguard.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MedicationErrorCode implements ResponseCode {
    MEDICATION_NOT_FOUND("404", "존재하지 않는 약 정보입니다."),
    MEDICATION_ALREADY_EXISTS("409", "이미 등록된 약 정보입니다."),
    MEDICATION_SEARCH_FAILED("500", "약 검색 중 오류가 발생했습니다.");

    private final String statusCode;
    private final String message;
}
