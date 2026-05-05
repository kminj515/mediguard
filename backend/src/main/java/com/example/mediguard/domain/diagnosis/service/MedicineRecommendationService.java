package com.example.mediguard.domain.diagnosis.service;

import com.example.mediguard.domain.diagnosis.dto.req.MedicineRecommendationReq;
import com.example.mediguard.domain.diagnosis.dto.res.MedicineRecommendationRes;
import com.example.mediguard.domain.diagnosis.entity.Medicine;
import com.example.mediguard.domain.diagnosis.entity.MedicineRecommendation;
import com.example.mediguard.domain.diagnosis.entity.Symptom;
import com.example.mediguard.domain.diagnosis.enums.MedicineErrorCode;
import com.example.mediguard.domain.diagnosis.enums.SymptomErrorCode;
import com.example.mediguard.domain.diagnosis.exception.MedicineException;
import com.example.mediguard.domain.diagnosis.exception.SymptomException;
import com.example.mediguard.domain.diagnosis.repository.MedicineRecommendationRepository;
import com.example.mediguard.domain.diagnosis.repository.MedicineRepository;
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
public class MedicineRecommendationService {

    private final MedicineRecommendationRepository recommendationRepository;
    private final MemberRepository memberRepository;
    private final MedicineRepository medicineRepository;
    private final SymptomRepository symptomRepository;

    // 추천 등록
    @Transactional
    public MedicineRecommendationRes createRecommendation(Long memberId, MedicineRecommendationReq req) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Symptom symptom = symptomRepository.findById(req.getSymptomId())
                .orElseThrow(() -> new SymptomException(SymptomErrorCode.SYMPTOM_NOT_FOUND));

        Medicine medicine = medicineRepository.findById(req.getMedicineId())
                .orElseThrow(() -> new MedicineException(MedicineErrorCode.MEDICINE_NOT_FOUND));

        MedicineRecommendation recommendation = MedicineRecommendation.builder()
                .member(member)
                .symptom(symptom)
                .medicine(medicine)
                .recommendationReason(req.getRecommendationReason())
                .confidenceScore(req.getConfidenceScore())
                .userAccepted(req.getUserAccepted())
                .build();

        return toRes(recommendationRepository.save(recommendation));
    }

    // 내 추천 이력 조회
    @Transactional(readOnly = true)
    public List<MedicineRecommendationRes> getMyRecommendations(Long memberId) {
        return recommendationRepository.findByMember_MemberIdOrderByCreatedAtDesc(memberId)
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    // 추천 단건 조회
    @Transactional(readOnly = true)
    public MedicineRecommendationRes getRecommendation(Long recommendationId) {
        MedicineRecommendation recommendation = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new MedicineException(MedicineErrorCode.MEDICINE_NOT_FOUND));
        return toRes(recommendation);
    }

    // Entity → Response DTO 변환
    private MedicineRecommendationRes toRes(MedicineRecommendation rec) {
        return MedicineRecommendationRes.builder()
                .recommendationId(rec.getRecommendationId())
                .memberId(rec.getMember().getMemberId())
                .memberNickname(rec.getMember().getNickname())
                .symptomId(rec.getSymptom().getSymptomId())
                .symptomType(rec.getSymptom().getSymptomType())
                .medicineId(rec.getMedicine().getMedicineId())
                .medicineName(rec.getMedicine().getName())
                .recommendationReason(rec.getRecommendationReason())
                .confidenceScore(rec.getConfidenceScore())
                .userAccepted(rec.isUserAccepted())
                .createdAt(rec.getCreatedAt())
                .updatedAt(rec.getUpdatedAt())
                .build();
    }
}