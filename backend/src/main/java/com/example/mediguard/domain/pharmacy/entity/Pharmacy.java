package com.example.mediguard.domain.pharmacy.entity;

import com.example.mediguard.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Pharmacy extends BaseEntity { // 기존 BaseEntity 상속 규칙 적용[cite: 5, 7]

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pharmacyId;

    @Column(nullable = false, length = 100)
    private String name; // 약국 이름

    @Column(nullable = false, length = 200)
    private String address; // 약국 주소

    @Column(nullable = false)
    private Double latitude; // 위도 (지도 API 연동용)

    @Column(nullable = false)
    private Double longitude; // 경도 (지도 API 연동용)

    @Column(length = 100)
    private String contact; // 전화번호

    @Column(name = "operating_hours", length = 200)
    private String operatingHours; // 영업 시간 안내

    @Column(name = "is_night_pharmacy", nullable = false)
    private boolean nightPharmacy; // 심야 약국 여부

    @Column(name = "is_twenty_four_hours", nullable = false)
    private boolean twentyFourHours; // 24시간 영업 여부
}