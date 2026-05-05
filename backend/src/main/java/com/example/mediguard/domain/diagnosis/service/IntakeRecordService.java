package com.example.mediguard.domain.diagnosis.service;

import com.example.mediguard.domain.diagnosis.dto.req.IntakeRecordReq;
import com.example.mediguard.domain.diagnosis.dto.res.IntakeRecordRes;
import com.example.mediguard.domain.diagnosis.entity.IntakeRecord;
import com.example.mediguard.domain.diagnosis.entity.Medicine;
import com.example.mediguard.domain.diagnosis.enums.IntakeRecordErrorCode;
import com.example.mediguard.domain.diagnosis.enums.MedicineErrorCode;
import com.example.mediguard.domain.diagnosis.exception.IntakeRecordException;
import com.example.mediguard.domain.diagnosis.exception.MedicineException;
import com.example.mediguard.domain.diagnosis.repository.IntakeRecordRepository;
import com.example.mediguard.domain.diagnosis.repository.MedicineRepository;
import com.example.mediguard.domain.member.entity.Member;
import com.example.mediguard.domain.member.enums.MemberErrorCode;
import com.example.mediguard.domain.member.exception.MemberException;
import com.example.mediguard.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IntakeRecordService {

    private final IntakeRecordRepository intakeRecordRepository;
    private final MedicineRepository medicineRepository;
    private final MemberRepository memberRepository;

    // 복약 기록 등록
    @Transactional
    public IntakeRecordRes createIntakeRecord(Long memberId, IntakeRecordReq req) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Medicine medicine = medicineRepository.findById(req.getMedicineId())
                .orElseThrow(() -> new MedicineException(MedicineErrorCode.MEDICINE_NOT_FOUND));

        IntakeRecord intakeRecord = IntakeRecord.builder()
                .member(member)
                .medicine(medicine)
                .intakeTime(req.getIntakeTime())
                .memo(req.getMemo())
                .isEmptyStomach(req.isEmptyStomach())
                .alertNeeded(req.isAlertNeeded())
                .build();

        return toRes(intakeRecordRepository.save(intakeRecord));
    }

    // 내 복약 기록 전체 조회
    @Transactional(readOnly = true)
    public List<IntakeRecordRes> getMyIntakeRecords(Long memberId) {
        return intakeRecordRepository.findByMember_MemberIdOrderByIntakeTimeDesc(memberId)
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    // 복약 기록 단건 조회
    @Transactional(readOnly = true)
    public IntakeRecordRes getIntakeRecord(Long intakeRecordId) {
        IntakeRecord intakeRecord = intakeRecordRepository.findById(intakeRecordId)
                .orElseThrow(() -> new IntakeRecordException(IntakeRecordErrorCode.INTAKE_RECORD_NOT_FOUND));
        return toRes(intakeRecord);
    }

    // Entity → Response DTO 변환
    private IntakeRecordRes toRes(IntakeRecord intakeRecord) {
        return IntakeRecordRes.builder()
                .intakeRecordId(intakeRecord.getIntakeRecordId())
                .memberId(intakeRecord.getMember().getMemberId())
                .memberNickname(intakeRecord.getMember().getNickname())
                .medicineId(intakeRecord.getMedicine().getMedicineId())
                .medicineName(intakeRecord.getMedicine().getName())
                .intakeTime(intakeRecord.getIntakeTime())
                .memo(intakeRecord.getMemo())
                .isEmptyStomach(intakeRecord.isEmptyStomach())
                .alertNeeded(intakeRecord.isAlertNeeded())
                .createdAt(intakeRecord.getCreatedAt())
                .updatedAt(intakeRecord.getUpdatedAt())
                .build();
    }
}