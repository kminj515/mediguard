package com.example.mediguard.domain.diagnosis.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum DiagnosisGrade {
    MASTER(90, "복약 안전 마스터"),
    SHIELD(70, "복약 안전 수호자"),
    NOVICE(50, "복약 안전 입문자"),
    CAUTION(0, "복약 주의 필요");

    private final int minScore;
    private final String description;

    public static DiagnosisGrade fromScore(int score) {
        return Arrays.stream(DiagnosisGrade.values())
                .filter(grade -> score >= grade.minScore)
                .findFirst()
                .orElse(CAUTION);
    }
}
