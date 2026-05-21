package com.example.mediguard.domain.diagnosis.service;

import com.example.mediguard.domain.chatbot.service.GeminiClientService;
import com.example.mediguard.domain.diagnosis.dto.res.MedicineScanRes;
import com.example.mediguard.domain.diagnosis.entity.Medicine;
import com.example.mediguard.domain.diagnosis.repository.MedicineRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicineScanService {

    private final GeminiClientService geminiClientService;
    private final MedicineRepository medicineRepository;
    private final ObjectMapper objectMapper;

    private static final String VISION_PROMPT = """
            이 이미지에서 약 봉투, 약통, 또는 처방전의 정보를 분석해주세요.
            반드시 아래 JSON 형식으로만 응답하고, 다른 설명은 절대 포함하지 마세요.

            {"medicineName":"약 이름","dosage":"복용법 (예: 1일 3회 식후 30분)","memo":"주의사항"}

            약 정보가 없거나 이미지가 약과 관련 없으면: {"medicineName":"","dosage":"","memo":""}
            """;

    public MedicineScanRes scanMedicineImage(MultipartFile image) throws Exception {
        // 1. 이미지 → base64 변환
        byte[] bytes = image.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";

        // 2. Gemini Vision 호출
        String rawResponse = geminiClientService.getCompletionWithImage(base64, mimeType, VISION_PROMPT);
        log.info("Gemini Vision 응답: {}", rawResponse);

        // 3. JSON 파싱
        String json = extractJson(rawResponse);
        JsonNode node = objectMapper.readTree(json);

        String medicineName = node.path("medicineName").asText("").trim();
        String dosage = node.path("dosage").asText("").trim();
        String memo = node.path("memo").asText("").trim();

        // 4. DB에서 약 이름 매칭 시도
        Medicine matched = findMatchingMedicine(medicineName);

        return MedicineScanRes.builder()
                .medicineId(matched != null ? matched.getMedicineId() : null)
                .medicineName(medicineName)
                .dosage(dosage)
                .memo(memo)
                .matched(matched != null)
                .build();
    }

    private Medicine findMatchingMedicine(String medicineName) {
        if (medicineName == null || medicineName.isBlank()) return null;

        // 정확히 일치하는 약 먼저 검색
        var exact = medicineRepository.findByName(medicineName);
        if (exact.isPresent()) return exact.get();

        // LIKE 검색으로 가장 가까운 것 반환
        List<Medicine> candidates = medicineRepository.findByNameContaining(medicineName);
        if (!candidates.isEmpty()) return candidates.get(0);

        // 이름의 첫 단어로 재시도
        String firstWord = medicineName.split("\\s+")[0];
        if (!firstWord.equals(medicineName)) {
            List<Medicine> byFirstWord = medicineRepository.findByNameContaining(firstWord);
            if (!byFirstWord.isEmpty()) return byFirstWord.get(0);
        }

        return null;
    }

    // Gemini 응답에서 JSON 블록 추출 (마크다운 코드블록 제거)
    private String extractJson(String raw) {
        Pattern pattern = Pattern.compile("\\{[^{}]*\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(raw);
        if (matcher.find()) return matcher.group();
        return "{\"medicineName\":\"\",\"dosage\":\"\",\"memo\":\"\"}";
    }
}