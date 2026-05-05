package com.example.mediguard.domain.diagnosis.service;

import com.example.mediguard.domain.diagnosis.dto.req.SymptomReq;
import com.example.mediguard.domain.diagnosis.dto.res.SymptomRes;
import com.example.mediguard.domain.diagnosis.entity.Symptom;
import com.example.mediguard.domain.diagnosis.enums.SymptomErrorCode;
import com.example.mediguard.domain.diagnosis.exception.SymptomException;
import com.example.mediguard.domain.diagnosis.repository.SymptomRepository;
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
public class SymptomService {

    private final SymptomRepository symptomRepository;
    private final MemberRepository memberRepository;

    // 증상 등록
    @Transactional
    public SymptomRes createSymptom(Long memberId, SymptomReq req) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Symptom symptom = Symptom.builder()
                .member(member)
                .symptomType(req.getSymptomType())
                .symptomDescription(req.getSymptomDescription())
                .severity(req.getSeverity())
                .isEmergency(false) // AI 분석 전 기본값
                .build();

        return toRes(symptomRepository.save(symptom));
    }

    // 내 증상 목록 조회
    @Transactional(readOnly = true)
    public List<SymptomRes> getMySymptoms(Long memberId) {
        return symptomRepository.findByMember_MemberIdOrderByCreatedAtDesc(memberId)
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    // 증상 단건 조회
    @Transactional(readOnly = true)
    public SymptomRes getSymptom(Long symptomId) {
        Symptom symptom = symptomRepository.findById(symptomId)
                .orElseThrow(() -> new SymptomException(SymptomErrorCode.SYMPTOM_NOT_FOUND));
        return toRes(symptom);
    }

    // Entity → Response DTO 변환
    private SymptomRes toRes(Symptom symptom) {
        return SymptomRes.builder()
                .symptomId(symptom.getSymptomId())
                .memberId(symptom.getMember().getMemberId())
                .memberNickname(symptom.getMember().getNickname())
                .symptomType(symptom.getSymptomType())
                .symptomDescription(symptom.getSymptomDescription())
                .aiSummary(symptom.getAiSummary())
                .severity(symptom.getSeverity())
                .isEmergency(symptom.isEmergency())
                .createdAt(symptom.getCreatedAt())
                .updatedAt(symptom.getUpdatedAt())
                .build();
    }
}