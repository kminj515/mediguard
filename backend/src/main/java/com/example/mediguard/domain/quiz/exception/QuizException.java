package com.example.mediguard.domain.quiz.exception;

import com.example.mediguard.domain.quiz.enums.QuizErrorCode;
import com.example.mediguard.global.exception.CustomException;

public class QuizException extends CustomException {

    public QuizException(QuizErrorCode errorCode) {
        super(errorCode);
    }

}
