package com.example.mediguard.domain.diagnosis.repository;

import com.example.mediguard.domain.diagnosis.entity.IntakeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IntakeRecordRepository extends JpaRepository<IntakeRecord, Long> {

    // 특정 회원의 복용 기록 전체 조회
    List<IntakeRecord> findByMember_MemberId(Long memberId);

    // 특정 회원의 복용 기록 최신순 조회
    List<IntakeRecord> findByMember_MemberIdOrderByIntakeTimeDesc(Long memberId);

    // 특정 약의 복용 기록 조회
    List<IntakeRecord> findByMedicine_MedicineId(Long medicineId);

    // 특정 회원 + 특정 약 복용 기록 조회
    List<IntakeRecord> findByMember_MemberIdAndMedicine_MedicineId(Long memberId, Long medicineId);

    // 알림이 필요한 복용 기록 조회
    List<IntakeRecord> findByAlertNeededTrue();

    // 특정 회원의 알림 필요 복용 기록 조회
    List<IntakeRecord> findByMember_MemberIdAndAlertNeededTrue(Long memberId);

    // 공복 복용 기록 조회
    List<IntakeRecord> findByMember_MemberIdAndIsEmptyStomachTrue(Long memberId);

    // 기간별 복용 기록 조회
    @Query("SELECT ir FROM IntakeRecord ir WHERE ir.member.memberId = :memberId " +
            "AND ir.intakeTime BETWEEN :startTime AND :endTime " +
            "ORDER BY ir.intakeTime DESC")
    List<IntakeRecord> findByMemberIdAndIntakeTimeRange(
            @Param("memberId") Long memberId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // 특정 회원의 복용 기록 개수
    long countByMember_MemberId(Long memberId);

    // 특정 약의 총 복용 횟수
    @Query("SELECT COUNT(ir) FROM IntakeRecord ir WHERE ir.medicine.medicineId = :medicineId")
    long countByMedicineId(@Param("medicineId") Long medicineId);
}
