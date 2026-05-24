package com.example.mediguard.domain.chatbot.service;

import com.example.mediguard.domain.chatbot.dto.req.ChatWithPharmacyReq;
import com.example.mediguard.domain.chatbot.dto.res.ChatWithPharmacyRes;
import com.example.mediguard.domain.pharmacy.dto.req.PharmacySearchReq;
import com.example.mediguard.domain.pharmacy.dto.res.PharmacyRes;
import com.example.mediguard.domain.pharmacy.service.PharmacyApiService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ResourceLoader resourceLoader;
    private final GeminiClientService geminiClientService;
    private final PharmacyApiService pharmacyService;

    @Value("${gemini.prompts.medication.system:classpath:prompts/medication-system.txt}")
    private String systemPromptPath;

    @Value("${gemini.prompts.medication.user:classpath:prompts/medication-user.txt}")
    private String userPromptPath;

    private String systemPrompt;
    private String userPromptTemplate;

    @PostConstruct
    public void loadPrompts() throws IOException {
        this.systemPrompt = loadResource(systemPromptPath);
        this.userPromptTemplate = loadResource(userPromptPath);
        log.info("MediGuard 복약 상담 프롬프트 로딩 완료");
    }

    public String generateMedicationContent(String userQuery, String analysisData,
                                            String ageGroup, String interests) {
        String userPrompt = buildPrompt(userQuery, analysisData, ageGroup, interests);
        return geminiClientService.getCompletion(systemPrompt, userPrompt);
    }

    // 약 추천 + 약국 연결 통합 메서드
    public ChatWithPharmacyRes recommendWithPharmacy(ChatWithPharmacyReq req) {
        // 1. Gemini 약 추천 응답
        String aiAnswer = generateMedicationContent(
                req.getPrompt(),
                null,
                Objects.requireNonNullElse(req.getAgeGroup(), "20-30대"),
                "약 추천"
        );

        // 2. 위치 정보 있으면 주변 약국 조회
        List<PharmacyRes> nearbyPharmacies = List.of();
        if (req.getLatitude() != null && req.getLongitude() != null) {
            PharmacySearchReq searchReq = PharmacySearchReq.builder()
                    .latitude(req.getLatitude())
                    .longitude(req.getLongitude())
                    .radius(Objects.requireNonNullElse(req.getRadius(), 5000.0))
                    .nightPharmacy(req.getNightPharmacy())
                    .twentyFourHours(req.getTwentyFourHours())
                    .build();

            nearbyPharmacies = pharmacyService.searchPharmacies(searchReq);
        }

        return ChatWithPharmacyRes.builder()
                .aiAnswer(aiAnswer)
                .nearbyPharmacies(nearbyPharmacies)
                .build();
    }

    private String buildPrompt(String userQuery, String analysisData,
                               String ageGroup, String interests) {
        return userPromptTemplate
                .replace("{ANALYSIS_DATA}", Objects.requireNonNullElse(analysisData, "분석 데이터 없음"))
                .replace("{USER_QUERY}", userQuery)
                .replace("{TIMESTAMP}", LocalDateTime.now().toString())
                .replace("{AGE_GROUP}", Objects.requireNonNullElse(ageGroup, "20-30대"))
                .replace("{INTERESTS}", Objects.requireNonNullElse(interests, "일반"));
    }

    private String loadResource(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        if (!resource.exists()) throw new IOException("Prompt file not found: " + path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }
}
