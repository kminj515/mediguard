package com.example.mediguard.domain.diagnosis.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineReq {

    @NotBlank(message = "약 이름을 입력해주세요")
    private String name; // 약 이름 (예: 타이레놀)

    @NotBlank(message = "분류를 입력해주세요")
    private String category; // 분류 (예: 해열진통제)

    @NotBlank(message = "효능을 입력해주세요")
    private String efficacy; // 효능 설명

    private String precautions; // 복용 시 주의사항 (선택)

    @NotNull(message = "공복 복용 가능 여부를 입력해주세요")
    private Boolean emptyStomachSafe; // 공복 복용 가능 여부

    @NotNull(message = "졸림 유발 여부를 입력해주세요")
    private Boolean drowsiness; // 졸림 유발 여부

    private String activeIngredient; // 주요 성분 (선택)

    private String sideEffects; // 부작용 정보 (선택)

    private String imageUrl; // 약 이미지 URL (선택)
}
