package com.example.mediguard.domain.quiz.repository;

import com.example.mediguard.domain.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizHistoryRepository extends JpaRepository<QuizHistory, Long> {
    List<QuizHistory> findByMember_MemberId(Long memberId);
    boolean existsByMember_MemberIdAndQuiz_QuizIdAndGainExpGreaterThan(
            Long memberId, Long quizId, int gainExp
    );
}

