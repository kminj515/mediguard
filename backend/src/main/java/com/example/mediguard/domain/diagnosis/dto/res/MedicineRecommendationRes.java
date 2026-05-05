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
public class MedicineRecommendationRes {

    private Long recommendationId;
    private Long memberId;               // 연관 Member ID
    private String memberNickname;       // 닉네임 (편의 제공)
    private Long symptomId;              // 연관 Symptom ID
    private String symptomType;          // 증상 유형 (편의 제공)
    private Long medicineId;             // 연관 Medicine ID
    private String medicineName;         // 약 이름 (편의 제공)
    private String recommendationReason; // AI 추천 이유
    private Double confidenceScore;      // AI 신뢰도 (0.0 ~ 1.0)
    private boolean userAccepted;        // 사용자 수용 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
