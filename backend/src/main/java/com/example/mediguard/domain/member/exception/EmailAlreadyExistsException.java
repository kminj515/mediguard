package com.example.mediguard.domain.member.exception;


import com.example.mediguard.domain.member.enums.MemberErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class EmailAlreadyExistsException extends CustomException {

    public EmailAlreadyExistsException() {
        super(MemberErrorCode.EMAIL_ALREADY_EXISTS);
    }

    public EmailAlreadyExistsException(String description) {
        super(MemberErrorCode.EMAIL_ALREADY_EXISTS, description);
    }
}