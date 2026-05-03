package com.example.mediguard.domain.diagnosis.controller;

import com.example.mediguard.domain.diagnosis.dto.req.DiagnosisRequest;
import com.example.mediguard.domain.diagnosis.dto.res.DiagnosisHistoryResponse;
import com.example.mediguard.domain.diagnosis.dto.res.DiagnosisPartResponse;
import com.example.mediguard.domain.diagnosis.dto.res.DiagnosisResponse;
import com.example.mediguard.domain.diagnosis.service.DiagnosisService;
import com.example.mediguard.global.jwt.MemberUserDetails;
import com.example.mediguard.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diagnoses")
@Tag(name = "역량진단", description = "역량진단 제출 및 이력 조회")
public class DiagnosisApiController {

    private final DiagnosisService diagnosisService;

    @GetMapping("/questions")
    @Operation(summary = "문제 조회", description = "역량진단 문제 조회")
    public ApiResponse<List<DiagnosisPartResponse>> getDiagnosisQuestions() {
        return ApiResponse.ok(diagnosisService.getDiagnosisQuestions());
    }


    @PostMapping("/submit")
    @Operation(summary = "제출", description = "역량진단 제출")
    public ApiResponse<DiagnosisResponse> submitDiagnosis(
            @AuthenticationPrincipal MemberUserDetails userDetail,
            @RequestBody DiagnosisRequest request
    ) {
        Long memberId = userDetail.getMember().getMemberId();
        DiagnosisResponse response = diagnosisService.submitDiagnosis(memberId, request);
        return ApiResponse.ok(response);
    }


    @GetMapping("/history/{historyId}")
    @Operation(summary = "이력 조회", description = "역량진단 제출 이력 조회")
    public ApiResponse<DiagnosisHistoryResponse> getHistory(@PathVariable Long historyId) {
        DiagnosisHistoryResponse response = diagnosisService.getDiagnosisHistory(historyId);
        return ApiResponse.ok(response);
    }

}