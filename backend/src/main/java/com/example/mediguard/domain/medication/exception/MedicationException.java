package com.example.mediguard.domain.medication.exception;

import com.example.mediguard.domain.medication.enums.MedicationErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class MedicationException extends CustomException {
    public MedicationException(MedicationErrorCode errorCode) {
        super(errorCode);
    }
}
