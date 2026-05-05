package com.example.mediguard.domain.diagnosis.service;

import com.example.mediguard.domain.diagnosis.dto.req.MedicineReq;
import com.example.mediguard.domain.diagnosis.dto.res.MedicineRes;
import com.example.mediguard.domain.diagnosis.entity.Medicine;
import com.example.mediguard.domain.diagnosis.enums.MedicineErrorCode;
import com.example.mediguard.domain.diagnosis.exception.MedicineException;
import com.example.mediguard.domain.diagnosis.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;

    // 약 전체 목록 조회
    @Transactional(readOnly = true)
    public List<MedicineRes> getAllMedicines() {
        return medicineRepository.findAll()
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    // 약 단건 조회
    @Transactional(readOnly = true)
    public MedicineRes getMedicine(Long medicineId) {
        Medicine medicine = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new MedicineException(MedicineErrorCode.MEDICINE_NOT_FOUND));
        return toRes(medicine);
    }

    // 약 이름 검색
    @Transactional(readOnly = true)
    public List<MedicineRes> searchMedicines(String keyword) {
        return medicineRepository.findByNameContaining(keyword)
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    // 카테고리별 조회
    @Transactional(readOnly = true)
    public List<MedicineRes> getMedicinesByCategory(String category) {
        return medicineRepository.findByCategory(category)
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    // 약 등록
    @Transactional
    public MedicineRes createMedicine(MedicineReq req) {
        Medicine medicine = Medicine.builder()
                .name(req.getName())
                .category(req.getCategory())
                .efficacy(req.getEfficacy())
                .precautions(req.getPrecautions())
                .emptyStomachSafe(req.getEmptyStomachSafe())
                .drowsiness(req.getDrowsiness())
                .activeIngredient(req.getActiveIngredient())
                .sideEffects(req.getSideEffects())
                .imageUrl(req.getImageUrl())
                .build();
        return toRes(medicineRepository.save(medicine));
    }

    // Entity → Response DTO 변환
    private MedicineRes toRes(Medicine medicine) {
        return MedicineRes.builder()
                .medicineId(medicine.getMedicineId())
                .name(medicine.getName())
                .category(medicine.getCategory())
                .efficacy(medicine.getEfficacy())
                .precautions(medicine.getPrecautions())
                .emptyStomachSafe(medicine.isEmptyStomachSafe())
                .drowsiness(medicine.isDrowsiness())
                .activeIngredient(medicine.getActiveIngredient())
                .sideEffects(medicine.getSideEffects())
                .imageUrl(medicine.getImageUrl())
                .createdAt(medicine.getCreatedAt())
                .updatedAt(medicine.getUpdatedAt())
                .build();
    }
}