package com.example.mediguard.domain.diagnosis.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntakeRecordReq {

    @NotNull(message = "약 ID를 입력해주세요")
    private Long medicineId; // 복용한 약 ID

    @NotNull(message = "복용 시간을 입력해주세요")
    private LocalDateTime intakeTime; // 실제 복용 시간

    private String memo; // 복용 관련 메모 (선택)

    private boolean isEmptyStomach; // 공복 복용 여부 (선택, 기본 false)

    @NotNull(message = "알림 필요 여부를 입력해주세요")
    private boolean alertNeeded; // 다음 복용 알림 필요 여부
}
