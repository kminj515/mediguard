package com.example.mediguard.domain.diagnosis.repository;

import com.example.mediguard.domain.diagnosis.entity.DiagnosisHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisHistoryRepository extends JpaRepository<DiagnosisHistory, Long> {
}
