package com.example.mediguard.domain.diagnosis.dto.res;

public record DiagnosisResponse(
        String grade,
        int totalScore,
        String message
) {}