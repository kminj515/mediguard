package com.example.guardpay.domain.diagnosis.entity;

import com.example.guardpay.domain.member.entity.Member; // Member는 다른 폴더에 있으니 import 필요
import com.example.guardpay.global.config.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class IntakeRecord extends BaseEntity { // 기존 BaseEntity 상속 규칙 적용[cite: 5, 7]

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long intakeRecordId;

    @ManyToOne(fetch = FetchType.LAZY) // 성능 최적화를 위한 지연 로딩[cite: 6, 7]
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // 성능 최적화를 위한 지연 로딩[cite: 6, 7]
    @JoinColumn(name = "medicine_id", nullable = false)
    private Medicine medicine; // 같은 폴더(패키지)에 있으므로 import 불필요!

    @Column(name = "intake_time", nullable = false)
    private LocalDateTime intakeTime; // 실제 복용 시간

    @Column(length = 250)
    private String memo; // 복용 관련 메모
}