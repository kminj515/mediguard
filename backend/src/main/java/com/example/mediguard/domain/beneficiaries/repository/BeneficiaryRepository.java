package com.example.mediguard.domain.beneficiaries.repository;

import com.example.mediguard.domain.beneficiaries.entity.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByActiveTrue();
}
