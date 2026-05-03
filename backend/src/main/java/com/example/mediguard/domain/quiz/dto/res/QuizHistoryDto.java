package com.example.mediguard.domain.quiz.dto.res;

import java.time.LocalDateTime;

public record QuizHistoryDto(Long quizId,
                             String question,
                             Integer gainExp,
                             LocalDateTime answerAt) {
}
