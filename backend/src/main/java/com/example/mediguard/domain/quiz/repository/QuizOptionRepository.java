package com.example.mediguard.domain.quiz.repository;

import com.example.mediguard.domain.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizOptionRepository extends JpaRepository<QuizOption, Long> {
    List<QuizOption> findByQuiz_QuizId(Long quizId);
}