package com.example.mediguard.domain.pharmacy.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyRes {

    private Long pharmacyId;
    private String name;             // 약국 이름
    private String address;          // 약국 주소
    private Double latitude;         // 위도
    private Double longitude;        // 경도
    private String contact;          // 전화번호
    private String operatingHours;   // 영업 시간
    private boolean nightPharmacy;   // 심야 약국 여부
    private boolean twentyFourHours; // 24시간 영업 여부
    private Double distance;         // 현재 위치로부터 거리 (m)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 거리 텍스트 변환 (예: 350m, 1.2km)
    public String getDistanceText() {
        if (distance == null) return null;
        if (distance < 1000) {
            return String.format("%.0fm", distance);
        } else {
            return String.format("%.1fkm", distance / 1000);
        }
    }
}
