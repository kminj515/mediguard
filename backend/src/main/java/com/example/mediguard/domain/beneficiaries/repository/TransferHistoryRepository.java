package com.example.mediguard.domain.beneficiaries.repository;

import com.example.mediguard.domain.beneficiaries.entity.TransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferHistoryRepository extends JpaRepository<TransferHistory, Long> {
}
