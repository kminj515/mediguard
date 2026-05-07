package com.example.mediguard.domain.medication.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "medication")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;           // 약 이름

    @Column(nullable = false)
    private String ingredient;     // 성분

    @Column(nullable = false)
    private String efficacy;       // 효능

    @Column(nullable = false)
    private String dosage;         // 복용법

    @Column(columnDefinition = "TEXT")
    private String precaution;     // 주의사항

    @Column(columnDefinition = "TEXT")
    private String sideEffect;     // 부작용
}
