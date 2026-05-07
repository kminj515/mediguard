package com.example.mediguard.domain.chatbot.dto.req;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatWithPharmacyReq {

    // 챗봇 입력
    private String prompt;       // 증상/질문
    private String ageGroup;     // 연령대 (선택)

    // 약국 검색 입력 (선택 - 없으면 약국 조회 스킵)
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitude;     // 현재 위도

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitude;    // 현재 경도

    private Double radius;          // 검색 반경 (기본 5000m)
    private Boolean nightPharmacy;  // 심야 약국 필터
    private Boolean twentyFourHours; // 24시간 약국 필터
}
