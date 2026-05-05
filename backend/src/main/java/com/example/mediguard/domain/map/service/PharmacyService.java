package com.example.mediguard.domain.map.service;

import com.example.mediguard.domain.map.converter.MapConverter;
import com.example.mediguard.domain.map.dto.req.PharmacySearchReq;
import com.example.mediguard.domain.map.dto.res.PharmacyRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyService {

    @Value("${kakao.rest-api-key}")
    private String kakaoRestApiKey;

    private final RestTemplate restTemplate;
    private final KakaoLocalClient kakaoLocalClient;

    public List<PharmacyRes> findPharmaciesNearby(PharmacySearchReq request) {

        // 1. API 호출
        List<Map<String, Object>> documents = kakaoLocalClient.searchKeywords(
                request.getPharmacyName(),
                request.getLatitude(),
                request.getLongitude(),
                request.getRadius()
        );

        return documents.stream()
                .filter(this::isPharmacyCategory)
                .map(MapConverter::toPharmacyRes)
                .toList();
    }

    /**
     * 약국 카테고리인지 확인
     */
    private boolean isPharmacyCategory(Map<String, Object> doc) {
        String category = (String) doc.get("category_name");
        return category != null && (category.contains("약국") || category.contains("의약품"));
    }

    /**
     * 장소명에서 약국명 추출
     * 예: "온누리약국 강남역점" -> "온누리약국"
     */
    private String extractPharmacyName(String placeName) {
        if (placeName == null) return "";

        String[] pharmacyNames = {
                "온누리약국",
                "한미약국",
                "종로약국",
                "메디팜약국",
                "드림약국",
                "건강약국",
                "365약국",
                "보람약국",
                "하나약국",
                "국민약국",
                "미래약국",
                "사랑약국"
        };

        for (String pharmacyName : pharmacyNames) {
            if (placeName.contains(pharmacyName)) {
                return pharmacyName;
            }
        }

        return placeName.split(" ")[0]; // 첫 단어 반환
    }

    /**
     * 장소명에서 지점명 추출
     * 예: "온누리약국 강남역점" -> "강남역점"
     */
    private String extractBranchName(String placeName) {
        if (placeName == null) return "";

        String pharmacyName = extractPharmacyName(placeName);
        return placeName.replace(pharmacyName, "").trim();
    }
}
