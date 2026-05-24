package com.example.mediguard.domain.chatbot.enums;

import com.example.mediguard.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements ResponseCode {
    CHAT_API_ERROR("503", "AI 서비스 응답 중 오류가 발생했습니다."),
    PROMPT_LOAD_FAILED("500", "프롬프트 템플릿을 불러오는 데 실패했습니다."),
    SAFETY_BLOCK("400", "안전 정책에 의해 답변이 제한되었습니다."),
    EMPTY_RESPONSE("500", "AI로부터 빈 응답을 받았습니다.");

    private final String statusCode;
    private final String message;
}