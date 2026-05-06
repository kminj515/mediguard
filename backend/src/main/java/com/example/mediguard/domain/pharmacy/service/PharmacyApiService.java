package com.example.mediguard.domain.pharmacy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.mediguard.domain.pharmacy.dto.req.PharmacySearchReq;
import com.example.mediguard.domain.pharmacy.dto.res.PharmacyRes;
import com.example.mediguard.domain.pharmacy.entity.Pharmacy;
import com.example.mediguard.domain.pharmacy.enums.PharmacyErrorCode;
import com.example.mediguard.domain.pharmacy.exception.PharmacyException;
import com.example.mediguard.domain.pharmacy.repository.PharmacyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyApiService {

    @Value("${pharmacy.api.key}")
    private String apiKey;

    @Value("${pharmacy.api.endpoint}")
    private String apiEndpoint;

    private final RestTemplate restTemplate;
    private final PharmacyRepository pharmacyRepository;

    @Transactional
    public void fetchAndSavePharmacies(String city, String district) {
        try {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl(apiEndpoint + "/getParmacyListInfoInqire")
                    .queryParam("serviceKey", apiKey)
                    .queryParam("Q0", city)
                    .queryParam("Q1", district)
                    .queryParam("pageNo", 1)
                    .queryParam("numOfRows", 100)
                    .build(false)
                    .encode(StandardCharsets.UTF_8)
                    .toUri();

            String response = restTemplate.getForObject(uri, String.class);
            log.info("📦 RAW API Response (앞 500자): {}",
                    response != null ? response.substring(0, Math.min(500, response.length())) : "null");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            JsonNode items = root.path("response").path("body").path("items").path("item");

            List<Pharmacy> pharmacies = new ArrayList<>();
            for (JsonNode item : items) {
                String name = item.path("dutyName").asText(null);
                String address = item.path("dutyAddr").asText(null);
                String contact = item.path("dutyTel1").asText(null);
                String latStr = item.path("wgs84Lat").asText(null);
                String lonStr = item.path("wgs84Lon").asText(null);

                if (name == null || address == null || latStr == null || lonStr == null) continue;

                pharmacies.add(Pharmacy.builder()
                        .name(name)
                        .address(address)
                        .contact(contact)
                        .latitude(Double.parseDouble(latStr))
                        .longitude(Double.parseDouble(lonStr))
                        .operatingHours(item.path("dutyTime1s").asText(null))
                        .nightPharmacy(false)
                        .twentyFourHours(false)
                        .build());
            }

            pharmacyRepository.saveAll(pharmacies);
            log.info("✅ 약국 정보 {}건 저장 완료", pharmacies.size());

        } catch (Exception e) {
            log.error("약국 API 호출 실패: {}", e.getMessage());
            throw new PharmacyException(PharmacyErrorCode.PHARMACY_NOT_FOUND);
        }
    }

    @Transactional(readOnly = true)
    public List<PharmacyRes> searchPharmacies(PharmacySearchReq req) {
        List<Pharmacy> pharmacies;

        if (Boolean.TRUE.equals(req.getTwentyFourHours())) {
            pharmacies = pharmacyRepository.findTwentyFourPharmaciesWithinRadius(
                    req.getLatitude(), req.getLongitude(), req.getRadius());
        } else if (Boolean.TRUE.equals(req.getNightPharmacy())) {
            pharmacies = pharmacyRepository.findNightPharmaciesWithinRadius(
                    req.getLatitude(), req.getLongitude(), req.getRadius());
        } else {
            pharmacies = pharmacyRepository.findPharmaciesWithinRadius(
                    req.getLatitude(), req.getLongitude(), req.getRadius());
        }

        return pharmacies.stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PharmacyRes getPharmacy(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new PharmacyException(PharmacyErrorCode.PHARMACY_NOT_FOUND));
        return toRes(pharmacy);
    }

    @Transactional(readOnly = true)
    public List<PharmacyRes> get24HourPharmacies() {
        return pharmacyRepository.findByTwentyFourHoursTrue()
                .stream().map(this::toRes).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PharmacyRes> getNightPharmacies() {
        return pharmacyRepository.findByNightPharmacyTrue()
                .stream().map(this::toRes).collect(Collectors.toList());
    }

    private PharmacyRes toRes(Pharmacy pharmacy) {
        return PharmacyRes.builder()
                .pharmacyId(pharmacy.getPharmacyId())
                .name(pharmacy.getName())
                .address(pharmacy.getAddress())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .contact(pharmacy.getContact())
                .operatingHours(pharmacy.getOperatingHours())
                .nightPharmacy(pharmacy.isNightPharmacy())
                .twentyFourHours(pharmacy.isTwentyFourHours())
                .createdAt(pharmacy.getCreatedAt())
                .updatedAt(pharmacy.getUpdatedAt())
                .build();
    }
}