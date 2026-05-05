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
public class IntakeRecordRes {

    private Long intakeRecordId;
    private Long memberId;            // 연관 Member ID
    private String memberNickname;    // 닉네임 (편의 제공)
    private Long medicineId;          // 연관 Medicine ID
    private String medicineName;      // 약 이름 (편의 제공)
    private LocalDateTime intakeTime; // 복용 시간
    private String memo;              // 복용 메모
    private boolean isEmptyStomach;   // 공복 복용 여부
    private boolean alertNeeded;      // 다음 복용 알림 필요 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
