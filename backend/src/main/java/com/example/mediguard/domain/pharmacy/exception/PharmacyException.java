package com.example.mediguard.domain.pharmacy.exception;

import com.example.mediguard.domain.pharmacy.enums.PharmacyErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class PharmacyException extends CustomException {

    public PharmacyException(PharmacyErrorCode errorCode) {
        super(errorCode);
    }
}
