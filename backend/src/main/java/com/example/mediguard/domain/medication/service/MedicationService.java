package com.example.mediguard.domain.medication.service;

import com.example.mediguard.domain.medication.dto.req.MedicationRequestDto;
import com.example.mediguard.domain.medication.dto.res.MedicationResponseDto;
import com.example.mediguard.domain.medication.entity.Medication;
import com.example.mediguard.domain.medication.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicationService {

    private final MedicationRepository medicationRepository;

    // 약 등록
    public MedicationResponseDto create(MedicationRequestDto dto) {
        Medication medication = Medication.builder()
                .name(dto.getName())
                .ingredient(dto.getIngredient())
                .efficacy(dto.getEfficacy())
                .dosage(dto.getDosage())
                .precaution(dto.getPrecaution())
                .sideEffect(dto.getSideEffect())
                .build();
        return MedicationResponseDto.from(medicationRepository.save(medication));
    }

    // 전체 조회
    public List<MedicationResponseDto> getAll() {
        return medicationRepository.findAll()
                .stream()
                .map(MedicationResponseDto::from)
                .toList();
    }

    // 단건 조회
    public MedicationResponseDto getOne(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 약입니다."));
        return MedicationResponseDto.from(medication);
    }

    // 이름으로 검색
    public List<MedicationResponseDto> searchByName(String name) {
        return medicationRepository.findByNameContaining(name)
                .stream()
                .map(MedicationResponseDto::from)
                .toList();
    }
}
