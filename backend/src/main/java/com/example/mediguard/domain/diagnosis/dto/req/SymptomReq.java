package com.example.mediguard.domain.diagnosis.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SymptomReq {

    @NotBlank(message = "주 증상을 입력해주세요")
    private String symptomType; // 주 증상 (예: 두통, 복통, 기침)

    @NotBlank(message = "증상 설명을 입력해주세요")
    private String symptomDescription; // 사용자가 입력한 자세한 설명

    @Pattern(regexp = "경증|중등도|중증", message = "심각도는 경증, 중등도, 중증 중 하나여야 합니다")
    private String severity; // 심각도 (선택 - AI가 판단할 수도 있음)
}
