package com.example.mediguard.domain.map.controller;

import com.example.mediguard.domain.map.dto.req.PharmacySearchReq;
import com.example.mediguard.domain.map.dto.res.PharmacyRes;
import com.example.mediguard.domain.map.service.PharmacyService;
import com.example.mediguard.global.jwt.MemberUserDetails;
import com.example.mediguard.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/pharmacies")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @Operation(summary = "주변 약국 검색 API", description = "사용자 위치 기준 반경 내 약국을 검색합니다.")
    @GetMapping("/nearby")
    public ApiResponse<List<PharmacyRes>> findNearbyPharmacies(
            @Valid @ModelAttribute PharmacySearchReq request,
            @AuthenticationPrincipal MemberUserDetails userDetails
    ) {
        List<PharmacyRes> pharmacies = pharmacyService.findPharmaciesNearby(request);
        return ApiResponse.ok(pharmacies);
    }
}
