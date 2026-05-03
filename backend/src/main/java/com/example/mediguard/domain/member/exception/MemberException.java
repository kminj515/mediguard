package com.example.mediguard.domain.member.exception;

import com.example.mediguard.domain.member.enums.MemberErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class MemberException extends CustomException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
