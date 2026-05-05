package com.example.mediguard.domain.diagnosis.exception;

import com.example.mediguard.domain.diagnosis.enums.SymptomErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class SymptomException extends CustomException {

    public SymptomException(SymptomErrorCode errorCode) {
        super(errorCode);
    }
}
