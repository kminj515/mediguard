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
public class Symptom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long symptomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 증상을 입력한 사용자

    @Column(name = "symptom_type", length = 50)
    private String symptomType; // 주 증상 (예: 두통, 복통, 기침)

    @Column(name = "symptom_description", columnDefinition = "TEXT")
    private String symptomDescription; // 사용자가 입력한 자세한 설명

    @Column(name = "ai_summary", columnDefinition = "TEXT")
    private String aiSummary; // AI가 분석한 증상 요약

    @Column(name = "severity", length = 20)
    private String severity; // 심각도 (경증/중등도/중증)

    @Column(name = "is_emergency", nullable = false)
    private boolean isEmergency; // 응급 상황 여부
}