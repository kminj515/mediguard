package com.example.mediguard.domain.medication.repository;

import com.example.mediguard.domain.medication.entity.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByNameContaining(String name);
    List<Medication> findByIngredientContaining(String ingredient);
}
