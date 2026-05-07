package com.example.mediguard.domain.medication.controller;

import com.example.mediguard.domain.medication.dto.req.MedicationRequestDto;
import com.example.mediguard.domain.medication.dto.res.MedicationResponseDto;
import com.example.mediguard.domain.medication.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
@RequiredArgsConstructor
@Tag(name = "Medication", description = "약 정보 API")
public class MedicationController {

    private final MedicationService medicationService;

    @PostMapping
    @Operation(summary = "약 등록")
    public ResponseEntity<MedicationResponseDto> create(@RequestBody MedicationRequestDto dto) {
        return ResponseEntity.ok(medicationService.create(dto));
    }

    @GetMapping
    @Operation(summary = "전체 약 목록 조회")
    public ResponseEntity<List<MedicationResponseDto>> getAll() {
        return ResponseEntity.ok(medicationService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "약 단건 조회")
    public ResponseEntity<MedicationResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(medicationService.getOne(id));
    }

    @GetMapping("/search")
    @Operation(summary = "약 이름으로 검색")
    public ResponseEntity<List<MedicationResponseDto>> search(@RequestParam String name) {
        return ResponseEntity.ok(medicationService.searchByName(name));
    }
}
