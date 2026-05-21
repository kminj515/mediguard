package com.example.mediguard.domain.diagnosis.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MedicineScanRes {
    private Long medicineId;       // DB에서 매칭된 약 ID (없으면 null)
    private String medicineName;   // AI가 인식한 약 이름
    private String dosage;         // 복용법
    private String memo;           // 주의사항
    private boolean matched;       // DB에서 약을 찾았는지 여부
}