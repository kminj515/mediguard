package com.example.mediguard.domain.diagnosis.controller;

import com.example.mediguard.domain.diagnosis.dto.req.MedicineReq;
import com.example.mediguard.domain.diagnosis.dto.res.MedicineRes;
import com.example.mediguard.domain.diagnosis.service.MedicineService;
import com.example.mediguard.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/medicines")
@Tag(name = "약 정보", description = "약 정보 조회 및 등록")
public class MedicineController {

    private final MedicineService medicineService;

    @GetMapping
    @Operation(summary = "약 전체 목록 조회")
    public ApiResponse<List<MedicineRes>> getAllMedicines() {
        return ApiResponse.ok(medicineService.getAllMedicines());
    }

    @GetMapping("/{medicineId}")
    @Operation(summary = "약 단건 조회")
    public ApiResponse<MedicineRes> getMedicine(@PathVariable Long medicineId) {
        return ApiResponse.ok(medicineService.getMedicine(medicineId));
    }

    @GetMapping("/search")
    @Operation(summary = "약 이름 검색")
    public ApiResponse<List<MedicineRes>> searchMedicines(@RequestParam String keyword) {
        return ApiResponse.ok(medicineService.searchMedicines(keyword));
    }

    @GetMapping("/category")
    @Operation(summary = "카테고리별 약 조회")
    public ApiResponse<List<MedicineRes>> getMedicinesByCategory(@RequestParam String category) {
        return ApiResponse.ok(medicineService.getMedicinesByCategory(category));
    }

    @PostMapping
    @Operation(summary = "약 등록")
    public ApiResponse<MedicineRes> createMedicine(@Valid @RequestBody MedicineReq req) {
        return ApiResponse.ok(medicineService.createMedicine(req));
    }
}