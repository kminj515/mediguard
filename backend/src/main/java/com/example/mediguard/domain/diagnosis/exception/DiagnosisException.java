package com.example.mediguard.domain.diagnosis.exception;

import com.example.mediguard.domain.diagnosis.enums.DiagnosisErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class DiagnosisException extends CustomException {

    public DiagnosisException(DiagnosisErrorCode errorCode) {
        super(errorCode);
    }

}
