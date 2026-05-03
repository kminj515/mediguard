package com.example.mediguard.domain.beneficiaries.dto.res;

public record BeneficiaryResponseDto(
        Long id,
        String nickname,
        String bankName,
        String accountNumber,
        String accountHolderName
) {}