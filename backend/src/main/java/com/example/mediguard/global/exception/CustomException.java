package com.example.mediguard.global.exception;

import com.example.mediguard.global.code.ResponseCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ResponseCode responseCode;
    private final String description;

    public CustomException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.description = null;
    }

    public CustomException(ResponseCode responseCode, String description) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.description = description;
    }
}