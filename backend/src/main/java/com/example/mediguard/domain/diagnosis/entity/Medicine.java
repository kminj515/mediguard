package com.example.mediguard.domain.diagnosis.entity;

import com.example.mediguard.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Medicine extends BaseEntity { // 기존 BaseEntity 상속 규칙 적용[cite: 5, 7]

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long medicineId;

    @Column(nullable = false, length = 100)
    private String name; // 약 이름 (예: 타이레놀)

    @Column(nullable = false, length = 50)
    private String category; // 분류 (예: 해열진통제)

    @Column(nullable = false, length = 250)
    private String efficacy; // 효능 (예: 발열, 두통 완화)

    @Column(length = 500)
    private String precautions; // 복용 시 주의사항

    @Column(name = "empty_stomach_safe", nullable = false)
    private boolean emptyStomachSafe; // 공복 복용 가능 여부

    @Column(nullable = false)
    private boolean drowsiness; // 졸림 유발 여부
}