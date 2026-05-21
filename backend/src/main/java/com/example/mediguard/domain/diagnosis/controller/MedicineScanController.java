package com.example.mediguard.domain.diagnosis.controller;

import com.example.mediguard.domain.diagnosis.dto.res.MedicineScanRes;
import com.example.mediguard.domain.diagnosis.service.MedicineScanService;
import com.example.mediguard.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/intake-records")
@Tag(name = "복약 기록", description = "복약 기록 등록 및 조회")
public class MedicineScanController {

    private final MedicineScanService medicineScanService;

    @PostMapping(value = "/scan", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "약 사진 AI 인식", description = "약 봉투/약통 사진을 업로드하면 AI가 약 이름과 복용법을 자동으로 인식합니다.")
    public ApiResponse<MedicineScanRes> scanMedicine(
            @RequestParam("image") MultipartFile image) throws Exception {
        return ApiResponse.ok(medicineScanService.scanMedicineImage(image));
    }
}