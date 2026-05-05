package com.example.mediguard.domain.diagnosis.controller;

import com.example.mediguard.domain.diagnosis.dto.req.SymptomReq;
import com.example.mediguard.domain.diagnosis.dto.res.SymptomRes;
import com.example.mediguard.domain.diagnosis.service.SymptomService;
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
@RequestMapping("/api/v1/symptoms")
@Tag(name = "증상", description = "증상 입력 및 조회")
public class SymptomController {

    private final SymptomService symptomService;

    @PostMapping
    @Operation(summary = "증상 등록")
    public ApiResponse<SymptomRes> createSymptom(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @Valid @RequestBody SymptomReq req) {
        Long memberId = userDetails.getMember().getMemberId();
        return ApiResponse.ok(symptomService.createSymptom(memberId, req));
    }

    @GetMapping
    @Operation(summary = "내 증상 목록 조회")
    public ApiResponse<List<SymptomRes>> getMySymptoms(
            @AuthenticationPrincipal MemberUserDetails userDetails) {
        Long memberId = userDetails.getMember().getMemberId();
        return ApiResponse.ok(symptomService.getMySymptoms(memberId));
    }

    @GetMapping("/{symptomId}")
    @Operation(summary = "증상 단건 조회")
    public ApiResponse<SymptomRes> getSymptom(@PathVariable Long symptomId) {
        return ApiResponse.ok(symptomService.getSymptom(symptomId));
    }
}