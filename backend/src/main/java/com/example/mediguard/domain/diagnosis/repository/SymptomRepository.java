package com.example.mediguard.domain.diagnosis.repository;

import com.example.mediguard.domain.diagnosis.entity.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {

    // 특정 회원의 증상 이력 전체 조회
    List<Symptom> findByMember_MemberId(Long memberId);

    // 특정 회원의 증상 이력 최신순 조회
    List<Symptom> findByMember_MemberIdOrderByCreatedAtDesc(Long memberId);

    // 응급 상황 증상만 조회
    List<Symptom> findByIsEmergencyTrue();

    // 특정 회원의 응급 증상 조회
    List<Symptom> findByMember_MemberIdAndIsEmergencyTrue(Long memberId);

    // 심각도별 조회
    List<Symptom> findBySeverity(String severity);

    // 특정 회원 + 특정 증상 유형 조회
    List<Symptom> findByMember_MemberIdAndSymptomType(Long memberId, String symptomType);

    // 기간별 증상 이력 조회
    @Query("SELECT s FROM Symptom s WHERE s.member.memberId = :memberId " +
            "AND s.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY s.createdAt DESC")
    List<Symptom> findByMemberIdAndDateRange(
            @Param("memberId") Long memberId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    // 특정 증상 유형의 발생 횟수 집계
    @Query("SELECT COUNT(s) FROM Symptom s WHERE s.member.memberId = :memberId " +
            "AND s.symptomType = :symptomType")
    long countByMemberIdAndSymptomType(
            @Param("memberId") Long memberId,
            @Param("symptomType") String symptomType
    );
}
