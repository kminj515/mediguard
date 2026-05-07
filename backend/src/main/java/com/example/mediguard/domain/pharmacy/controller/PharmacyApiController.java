package com.example.mediguard.domain.pharmacy.controller;

import com.example.mediguard.domain.pharmacy.dto.req.PharmacySearchReq;
import com.example.mediguard.domain.pharmacy.dto.res.PharmacyRes;
import com.example.mediguard.domain.pharmacy.service.PharmacyApiService;
import com.example.mediguard.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pharmacies")
@Tag(name = "약국", description = "주변 약국 조회")
public class PharmacyApiController {

    private final PharmacyApiService pharmacyService;

    @PostMapping("/search")
    @Operation(summary = "반경 내 약국 검색", description = "현재 위치 기준 반경 내 약국을 검색합니다.")
    public ApiResponse<List<PharmacyRes>> searchPharmacies(
            @Valid @RequestBody PharmacySearchReq req) {
        return ApiResponse.ok(pharmacyService.searchPharmacies(req));
    }

    @GetMapping("/{pharmacyId}")
    @Operation(summary = "약국 단건 조회")
    public ApiResponse<PharmacyRes> getPharmacy(@PathVariable Long pharmacyId) {
        return ApiResponse.ok(pharmacyService.getPharmacy(pharmacyId));
    }

    @GetMapping("/24hours")
    @Operation(summary = "24시간 약국 조회")
    public ApiResponse<List<PharmacyRes>> get24HourPharmacies() {
        return ApiResponse.ok(pharmacyService.get24HourPharmacies());
    }

    @GetMapping("/night")
    @Operation(summary = "심야 약국 조회")
    public ApiResponse<List<PharmacyRes>> getNightPharmacies() {
        return ApiResponse.ok(pharmacyService.getNightPharmacies());
    }

    @PostMapping("/fetch")
    @Operation(summary = "공공API 약국 데이터 수집", description = "공공데이터 API를 호출하여 약국 정보를 DB에 저장합니다.")
    public ApiResponse<Void> fetchPharmacies(
            @RequestParam String city,
            @RequestParam String district) {
        pharmacyService.fetchAndSavePharmacies(city, district);
        return ApiResponse.ok();
    }
}