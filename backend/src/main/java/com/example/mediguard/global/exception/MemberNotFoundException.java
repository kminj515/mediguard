package com.example.mediguard.global.exception;

import com.example.mediguard.domain.member.enums.MemberErrorCode;

public class MemberNotFoundException extends CustomException {
    public MemberNotFoundException() {
        super(MemberErrorCode.MEMBER_NOT_FOUND);
    }
}