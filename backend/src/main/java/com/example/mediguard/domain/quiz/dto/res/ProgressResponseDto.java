package com.example.mediguard.domain.quiz.dto.res;

public record ProgressResponseDto(
        Long categoryId,
        String categoryName,
        Integer progress
) {}