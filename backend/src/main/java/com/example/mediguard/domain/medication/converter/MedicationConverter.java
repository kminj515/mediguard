package com.example.mediguard.domain.medication.converter;

import com.example.mediguard.domain.medication.dto.res.MedicationResponseDto;
import com.example.mediguard.domain.medication.entity.Medication;

import java.util.List;

public class MedicationConverter {

    public static MedicationResponseDto toResponseDto(Medication medication) {
        return MedicationResponseDto.from(medication);
    }

    public static List<MedicationResponseDto> toResponseDtoList(List<Medication> medications) {
        return medications.stream()
                .map(MedicationConverter::toResponseDto)
                .toList();
    }
}
