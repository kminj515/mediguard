package com.example.mediguard.domain.beneficiaries.controller;

import com.example.mediguard.domain.beneficiaries.dto.req.TransferRequest;
import com.example.mediguard.domain.beneficiaries.dto.res.BeneficiaryResponseDto;
import com.example.mediguard.domain.beneficiaries.dto.res.TransferResponse;
import com.example.mediguard.domain.beneficiaries.service.BeneficiaryService;
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
@RequestMapping("/api/v1/beneficiaries")
@Tag(name = "가상 계좌", description = "가상 계좌 조회 및 송금 훈련 API")
public class BeneficiaryApiController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping("/random")
    @Operation(summary = "랜덤 가상계좌 조회", description = "하루 1회 갱신되는 랜덤 가상계좌 4개 조회")
    public ApiResponse<List<BeneficiaryResponseDto>> getRandomBeneficiaries(@AuthenticationPrincipal  MemberUserDetails userDetail) {
        Long memberId = userDetail.getMember().getMemberId();
        List<BeneficiaryResponseDto> reponses=beneficiaryService.getRandomBeneficiaries(memberId);
        return ApiResponse.ok(reponses);
    }


    @GetMapping("/{id}")
    @Operation(summary = "가상계좌 상세 조회", description = "선택한 가상계좌의 상세 정보 조회")
    public ApiResponse<BeneficiaryResponseDto> getBeneficiaryDetail(
            @AuthenticationPrincipal MemberUserDetails userDetail,
            @PathVariable Long id
    ) {
        Long memberId = userDetail.getMember().getMemberId();
        BeneficiaryResponseDto response = beneficiaryService.getBeneficiaryDetail(memberId, id);
        return ApiResponse.ok(response);
    }


    @PostMapping("/{beneficiaryId}/transfer")
    @Operation(summary = "모의 송금", description = "포인트 송금 후 리워드 지급")
    public ApiResponse<TransferResponse> transfer(
                                                   @PathVariable Long beneficiaryId,
                                                   @RequestBody TransferRequest request,
                                                   @AuthenticationPrincipal MemberUserDetails userDetails
    ) {
        Long memberId = userDetails.getMember().getMemberId();

        TransferResponse response = beneficiaryService.transfer(
                memberId,
                beneficiaryId,
                request.amount()
        );

        return ApiResponse.ok(response);
    }

}
