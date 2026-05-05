package com.example.mediguard.domain.diagnosis.dto.req;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRecommendationReq {

    @NotNull(message = "회원 ID를 입력해주세요")
    private Long memberId; // 추천받는 사용자 ID

    @NotNull(message = "증상 ID를 입력해주세요")
    private Long symptomId; // 입력한 증상 ID

    @NotNull(message = "약 ID를 입력해주세요")
    private Long medicineId; // AI가 추천한 약 ID

    private String recommendationReason; // AI 추천 이유 (선택)

    @DecimalMin(value = "0.0", message = "신뢰도는 0.0 이상이어야 합니다")
    @DecimalMax(value = "1.0", message = "신뢰도는 1.0 이하여야 합니다")
    private Double confidenceScore; // AI 신뢰도 (선택)

    @NotNull(message = "추천 수용 여부를 입력해주세요")
    private Boolean userAccepted; // 사용자가 추천을 수용했는지 여부
}
