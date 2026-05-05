package com.example.mediguard.domain.diagnosis.repository;

import com.example.mediguard.domain.diagnosis.entity.MedicineRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicineRecommendationRepository extends JpaRepository<MedicineRecommendation, Long> {

    // 특정 회원의 추천 이력 전체 조회
    List<MedicineRecommendation> findByMember_MemberId(Long memberId);

    // 특정 회원의 추천 이력 최신순 조회
    List<MedicineRecommendation> findByMember_MemberIdOrderByCreatedAtDesc(Long memberId);

    // 특정 증상에 대한 추천 이력 조회
    List<MedicineRecommendation> findBySymptom_SymptomId(Long symptomId);

    // 특정 약의 추천 이력 조회
    List<MedicineRecommendation> findByMedicine_MedicineId(Long medicineId);

    // 사용자가 수용한 추천만 조회
    List<MedicineRecommendation> findByMember_MemberIdAndUserAcceptedTrue(Long memberId);

    // 사용자가 거절한 추천만 조회
    List<MedicineRecommendation> findByMember_MemberIdAndUserAcceptedFalse(Long memberId);

    // 신뢰도 높은 순으로 추천 조회
    @Query("SELECT mr FROM MedicineRecommendation mr WHERE mr.member.memberId = :memberId " +
            "ORDER BY mr.confidenceScore DESC")
    List<MedicineRecommendation> findByMemberIdOrderByConfidenceScoreDesc(
            @Param("memberId") Long memberId
    );

    // 특정 신뢰도 이상의 추천만 조회
    @Query("SELECT mr FROM MedicineRecommendation mr " +
            "WHERE mr.member.memberId = :memberId " +
            "AND mr.confidenceScore >= :minScore " +
            "ORDER BY mr.confidenceScore DESC")
    List<MedicineRecommendation> findByMemberIdAndMinConfidenceScore(
            @Param("memberId") Long memberId,
            @Param("minScore") Double minScore
    );

    // 특정 회원의 추천 수용률 계산
    @Query("SELECT COUNT(mr) FROM MedicineRecommendation mr " +
            "WHERE mr.member.memberId = :memberId AND mr.userAccepted = true")
    long countAcceptedByMemberId(@Param("memberId") Long memberId);
}
