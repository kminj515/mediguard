package com.example.mediguard.domain.chatbot.controller;

import com.example.mediguard.domain.chatbot.dto.req.ChatRequestDto;
import com.example.mediguard.domain.chatbot.dto.req.ChatWithPharmacyReq;
import com.example.mediguard.domain.chatbot.dto.res.ChatResponseDto;
import com.example.mediguard.domain.chatbot.dto.res.ChatWithPharmacyRes;
import com.example.mediguard.domain.chatbot.service.ChatbotService;
import com.example.mediguard.domain.pharmacy.dto.res.PharmacyRes;
import com.example.mediguard.global.jwt.MemberUserDetails;
import com.example.mediguard.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "챗봇", description = "MediGuard 복약 상담 AI")
public class ChatBotController {

    private final ChatbotService chatbotService;

    @Operation(summary = "복약 상담 API", description = "복약 관련 질문을 입력하면 AI가 답변합니다.")
    @PostMapping("/chat")
    public ApiResponse<ChatResponseDto> chat(
            @RequestBody ChatRequestDto requestDto,
            @AuthenticationPrincipal MemberUserDetails userDetails) {

        String responseText = chatbotService.generateMedicationContent(
                requestDto.getPrompt(),
                null,
                "20-30대",
                "일반 복약 상담"
        );

        return ApiResponse.ok(new ChatResponseDto(responseText));
    }

    @Operation(summary = "약 추천 + 주변 약국 연결 API",
            description = "증상을 입력하면 약 추천 후 주변 약국 목록을 함께 반환합니다.")
    @PostMapping("/recommend-with-pharmacy")
    public ApiResponse<ChatWithPharmacyRes> recommendWithPharmacy(
            @RequestBody ChatWithPharmacyReq requestDto,
            @AuthenticationPrincipal MemberUserDetails userDetails) {

        return ApiResponse.ok(chatbotService.recommendWithPharmacy(requestDto));
    }
}
