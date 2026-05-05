package com.example.mediguard.domain.diagnosis.controller;

import com.example.mediguard.domain.diagnosis.dto.req.MedicineRecommendationReq;
import com.example.mediguard.domain.diagnosis.dto.res.MedicineRecommendationRes;
import com.example.mediguard.domain.diagnosis.service.MedicineRecommendationService;
import com.example.mediguard.global.jwt.MemberUserDetails;
import com.example.mediguard.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendations")
@Tag(name = "약 추천", description = "AI 약 추천 등록 및 조회")
public class MedicineRecommendationController {

    private final MedicineRecommendationService recommendationService;

    @PostMapping
    @Operation(summary = "약 추천 등록")
    public ApiResponse<MedicineRecommendationRes> createRecommendation(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @Valid @RequestBody MedicineRecommendationReq req) {
        Long memberId = userDetails.getMember().getMemberId();
        return ApiResponse.ok(recommendationService.createRecommendation(memberId, req));
    }

    @GetMapping
    @Operation(summary = "내 추천 이력 조회")
    public ApiResponse<List<MedicineRecommendationRes>> getMyRecommendations(
            @AuthenticationPrincipal MemberUserDetails userDetails) {
        Long memberId = userDetails.getMember().getMemberId();
        return ApiResponse.ok(recommendationService.getMyRecommendations(memberId));
    }

    @GetMapping("/{recommendationId}")
    @Operation(summary = "추천 단건 조회")
    public ApiResponse<MedicineRecommendationRes> getRecommendation(
            @PathVariable Long recommendationId) {
        return ApiResponse.ok(recommendationService.getRecommendation(recommendationId));
    }
}