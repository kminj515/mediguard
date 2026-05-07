package com.example.mediguard.domain.pharmacy.dto.req;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacySearchReq {

    @NotNull(message = "위도를 입력해주세요")
    @DecimalMin(value = "-90.0", message = "위도는 -90 ~ 90 사이여야 합니다")
    @DecimalMax(value = "90.0", message = "위도는 -90 ~ 90 사이여야 합니다")
    private Double latitude; // 현재 위치 위도

    @NotNull(message = "경도를 입력해주세요")
    @DecimalMin(value = "-180.0", message = "경도는 -180 ~ 180 사이여야 합니다")
    @DecimalMax(value = "180.0", message = "경도는 -180 ~ 180 사이여야 합니다")
    private Double longitude; // 현재 위치 경도

    @Min(value = 100, message = "최소 반경은 100m입니다")
    @Max(value = 10000, message = "최대 반경은 10km입니다")
    @Builder.Default
    private Double radius = 5000.0; // 검색 반경 (기본값 5km)

    private Boolean nightPharmacy;   // 심야 약국만 필터링 (선택)
    private Boolean twentyFourHours; // 24시간 약국만 필터링 (선택)
}
