package com.example.mediguard.domain.chatbot.exception;

import com.example.mediguard.domain.chatbot.enums.ChatErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class ChatException extends CustomException {
    public ChatException(ChatErrorCode errorCode) {
        super(errorCode);
    }
}
