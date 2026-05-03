package com.example.mediguard.domain.diagnosis.converter;

import com.example.mediguard.domain.diagnosis.dto.res.DiagnosisOptionDto;
import com.example.mediguard.domain.diagnosis.dto.res.DiagnosisQuestionDto;
import com.example.mediguard.domain.quiz.entity.Quiz;

import java.util.List;

public class DiagnosisConverter {
    public static DiagnosisQuestionDto toDiagnosisQuestionDto(Quiz quiz) {
        List<DiagnosisOptionDto> options = quiz.getOptions().stream()
                .map(opt -> new DiagnosisOptionDto(opt.getOptionId(), opt.getOptionText()))
                .toList();

        return new DiagnosisQuestionDto(
                quiz.getQuizId(),
                quiz.getQuestion(),
                options
        );
    }
}
