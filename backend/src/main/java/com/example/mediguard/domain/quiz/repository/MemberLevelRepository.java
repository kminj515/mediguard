package com.example.mediguard.domain.quiz.repository;

import com.example.mediguard.domain.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberLevelRepository extends JpaRepository<MemberLevel, Long> {
    List<MemberLevel> findByMember_MemberId(Long memberId);
}
