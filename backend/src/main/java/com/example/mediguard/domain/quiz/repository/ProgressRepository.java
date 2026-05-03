package com.example.mediguard.domain.quiz.repository;

import com.example.mediguard.domain.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByMember_MemberId(Long memberId);
    Optional<Progress> findByMember_MemberIdAndCategory_CategoryId(Long memberId, Long categoryId);
}
