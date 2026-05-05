package com.example.mediguard.domain.map.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacyRes {

    private Long id;
    private String name;            // 약국명 (예: "온누리약국")
    private String branchName;      // 지점명 (예: "강남역점")
    private String fullName;        // 전체명 (예: "온누리약국 강남역점")
    private String address;         // 지번 주소
    private String roadAddress;     // 도로명 주소
    private Double lat;             // 위도
    private Double lon;             // 경도
    private String phoneNumber;     // 전화번호
    private String businessHours;   // 영업시간
    private Double distance;        // 거리(m)

    // 거리를 읽기 쉬운 형식으로 반환
    public String getDistanceText() {
        if (distance == null) return null;
        if (distance < 1000) {
            return String.format("%.0fm", distance);
        } else {
            return String.format("%.1fkm", distance / 1000);
        }
    }
}
