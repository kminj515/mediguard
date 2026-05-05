package com.example.mediguard.global.exception;

import com.example.mediguard.domain.member.enums.MemberErrorCode;

public class InvalidPasswordException extends CustomException {
    public InvalidPasswordException() {
        super(MemberErrorCode.INVALID_PASSWORD);
    }
}