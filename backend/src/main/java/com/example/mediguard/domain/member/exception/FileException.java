package com.example.mediguard.domain.member.exception;

import com.example.mediguard.domain.member.enums.FileErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class FileException extends CustomException {
    public FileException(FileErrorCode errorCode) {
        super(errorCode);
    }}
