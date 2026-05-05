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
public class MedicineRes {

    private Long medicineId;
    private String name;              // 약 이름
    private String category;          // 분류
    private String efficacy;          // 효능
    private String precautions;       // 주의사항
    private boolean emptyStomachSafe; // 공복 복용 가능 여부
    private boolean drowsiness;       // 졸림 유발 여부
    private String activeIngredient;  // 주요 성분
    private String sideEffects;       // 부작용 정보
    private String imageUrl;          // 약 이미지 URL
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
