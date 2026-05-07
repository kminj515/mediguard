package com.example.mediguard.domain.chatbot.dto.res;

import com.example.mediguard.domain.pharmacy.dto.res.PharmacyRes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "약 추천 + 주변 약국 통합 응답")
public class ChatWithPharmacyRes {

    @Schema(description = "AI 복약 상담 응답 (Markdown 표 포함)")
    private String aiAnswer;

    @Schema(description = "주변 약국 목록 (위치 미제공 시 빈 배열)")
    private List<PharmacyRes> nearbyPharmacies;

    @Builder.Default
    @Schema(description = "응답 생성 시간")
    private LocalDateTime createdAt = LocalDateTime.now();
}
