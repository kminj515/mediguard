package com.example.mediguard.domain.beneficiaries.exception;

import com.example.mediguard.domain.beneficiaries.enums.BeneficiaryErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class BeneficiaryException extends CustomException {
    public BeneficiaryException(BeneficiaryErrorCode errorCode) {
        super(errorCode);
    }
}
