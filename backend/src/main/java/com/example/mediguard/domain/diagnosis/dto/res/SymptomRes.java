package com.example.mediguard.domain.diagnosis.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymptomRes {

    private Long symptomId;
    private Long memberId;              // 연관 Member ID
    private String memberNickname;      // 닉네임 (편의 제공)
    private String symptomType;         // 주 증상
    private String symptomDescription; // 자세한 설명
    private String aiSummary;           // AI 분석 요약
    private String severity;            // 심각도
    private boolean isEmergency;        // 응급 상황 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
