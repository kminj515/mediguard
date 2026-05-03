package com.example.mediguard.domain.quiz.dto.res;

public record QuizResponseDto(
        Long quizId,
        String question,
        Integer level,
        Integer point)
{ }
