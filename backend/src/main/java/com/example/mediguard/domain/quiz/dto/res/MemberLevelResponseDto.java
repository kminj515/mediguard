package com.example.mediguard.domain.quiz.dto.res;

public record MemberLevelResponseDto(
        Long categoryId,
        String categoryName,
        Integer level
) {}