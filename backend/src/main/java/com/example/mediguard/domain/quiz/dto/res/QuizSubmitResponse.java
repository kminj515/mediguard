package com.example.mediguard.domain.quiz.dto.res;

public record QuizSubmitResponse(
        boolean isCorrect,
        int gainExp,
        String message
) {}
