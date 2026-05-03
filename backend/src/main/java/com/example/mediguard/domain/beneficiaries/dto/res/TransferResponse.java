package com.example.mediguard.domain.beneficiaries.dto.res;

public record TransferResponse(
        Long transactionId,
        int updatedBalance,
        int reward
) {}