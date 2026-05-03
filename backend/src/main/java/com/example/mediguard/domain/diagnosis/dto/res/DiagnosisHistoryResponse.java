package com.example.mediguard.domain.diagnosis.dto.res;

public record DiagnosisHistoryResponse(
        Long historyId,
        int score,
        String finalGrade
) {}