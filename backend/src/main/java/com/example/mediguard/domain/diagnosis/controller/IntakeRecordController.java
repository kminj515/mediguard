package com.example.mediguard.domain.diagnosis.controller;

import com.example.mediguard.domain.diagnosis.dto.req.IntakeRecordReq;
import com.example.mediguard.domain.diagnosis.dto.res.IntakeRecordRes;
import com.example.mediguard.domain.diagnosis.service.IntakeRecordService;
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
@RequestMapping("/api/v1/intake-records")
@Tag(name = "복약 기록", description = "복약 기록 등록 및 조회")
public class IntakeRecordController {

    private final IntakeRecordService intakeRecordService;

    @PostMapping
    @Operation(summary = "복약 기록 등록")
    public ApiResponse<IntakeRecordRes> createIntakeRecord(
            @AuthenticationPrincipal MemberUserDetails userDetails,
            @Valid @RequestBody IntakeRecordReq req) {
        Long memberId = userDetails.getMember().getMemberId();
        return ApiResponse.ok(intakeRecordService.createIntakeRecord(memberId, req));
    }

    @GetMapping
    @Operation(summary = "내 복약 기록 전체 조회")
    public ApiResponse<List<IntakeRecordRes>> getMyIntakeRecords(
            @AuthenticationPrincipal MemberUserDetails userDetails) {
        Long memberId = userDetails.getMember().getMemberId();
        return ApiResponse.ok(intakeRecordService.getMyIntakeRecords(memberId));
    }

    @GetMapping("/{intakeRecordId}")
    @Operation(summary = "복약 기록 단건 조회")
    public ApiResponse<IntakeRecordRes> getIntakeRecord(@PathVariable Long intakeRecordId) {
        return ApiResponse.ok(intakeRecordService.getIntakeRecord(intakeRecordId));
    }
}