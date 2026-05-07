package com.example.mediguard.domain.chatbot.enums;

import com.example.mediguard.global.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements ResponseCode {
    // CHAT_001 -> 503 (외부 AI 서버 응답 불가)
    CHAT_API_ERROR("503", "AI 서비스 응답 중 오류가 발생했습니다."),

    // CHAT_002 -> 500 (내부 서버 에러 - 파일 못 찾음)
    PROMPT_LOAD_FAILED("500", "프롬프트 템플릿을 불러오는 데 실패했습니다."),

    // CHAT_003 -> 400 (잘못된 요청 - 안전 정책 위반)
    SAFETY_BLOCK("400", "안전 정책에 의해 답변이 제한되었습니다."),

    // CHAT_004 -> 500 (내부 서버 에러 - 빈 응답)
    EMPTY_RESPONSE("500", "AI로부터 빈 응답을 받았습니다.");

    private final String statusCode;
    private final String message;
}