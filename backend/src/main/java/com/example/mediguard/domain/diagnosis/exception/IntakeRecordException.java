package com.example.mediguard.domain.diagnosis.exception;

import com.example.mediguard.domain.diagnosis.enums.IntakeRecordErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class IntakeRecordException extends CustomException {

    public IntakeRecordException(IntakeRecordErrorCode errorCode) {
        super(errorCode);
    }
}
