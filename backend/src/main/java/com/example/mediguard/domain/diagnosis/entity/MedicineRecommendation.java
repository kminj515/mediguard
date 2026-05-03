package com.example.mediguard.domain.diagnosis.entity;

import com.example.mediguard.domain.member.entity.Member;
import com.example.mediguard.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MedicineRecommendation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 추천받은 사용자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symptom_id", nullable = false)
    private Symptom symptom; // 입력한 증상

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine; // AI가 추천한 약

    @Column(name = "recommendation_reason", columnDefinition = "TEXT")
    private String recommendationReason; // AI가 추천한 이유

    @Column(name = "confidence_score")
    private Double confidenceScore; // AI 신뢰도 (0.0 ~ 1.0)

    @Column(name = "user_accepted", nullable = false)
    private boolean userAccepted; // 사용자가 추천을 수용했는지 여부
}