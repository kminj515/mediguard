package com.example.mediguard.domain.pharmacy.service;

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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PharmacyService {

    @Value("${pharmacy.api.key}")
    private String apiKey;

    @Value("${pharmacy.api.endpoint}")
    private String apiEndpoint;

    private final RestTemplate restTemplate;
    private final PharmacyRepository pharmacyRepository;

    // 공공API 호출 → DB 저장
    @Transactional
    public void fetchAndSavePharmacies(String city, String district) {
        String url = apiEndpoint + "/getErmctInsttInfoInqire"
                + "?serviceKey=" + apiKey
                + "&Q0=" + city
                + "&Q1=" + district
                + "&pageNo=1"
                + "&numOfRows=100";

        try {
            String xmlResponse = restTemplate.getForObject(url, String.class);
            List<Pharmacy> pharmacies = parseXmlToPharmacies(xmlResponse);
            pharmacyRepository.saveAll(pharmacies);
            log.info("✅ 약국 정보 {}건 저장 완료", pharmacies.size());
        } catch (Exception e) {
            log.error("❌ 약국 API 호출 실패: {}", e.getMessage());
            throw new PharmacyException(PharmacyErrorCode.PHARMACY_NOT_FOUND);
        }
    }

    // XML 파싱
    private List<Pharmacy> parseXmlToPharmacies(String xml) throws Exception {
        List<Pharmacy> pharmacies = new ArrayList<>();
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        NodeList items = doc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            String name = getTagValue("dutyName", item);
            String address = getTagValue("dutyAddr", item);
            String contact = getTagValue("dutyTel1", item);
            String latStr = getTagValue("wgs84Lat", item);
            String lonStr = getTagValue("wgs84Lon", item);

            if (name == null || address == null || latStr == null || lonStr == null) continue;

            pharmacies.add(Pharmacy.builder()
                    .name(name)
                    .address(address)
                    .contact(contact)
                    .latitude(Double.parseDouble(latStr))
                    .longitude(Double.parseDouble(lonStr))
                    .operatingHours(getTagValue("dutyTime1s", item))
                    .nightPharmacy(false)
                    .twentyFourHours(false)
                    .build());
        }
        return pharmacies;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) return null;
        return nodeList.item(0).getTextContent();
    }

    // 반경 내 약국 조회
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
                .map(pharmacy -> {
                    double distance = calculateDistance(
                            req.getLatitude(), req.getLongitude(),
                            pharmacy.getLatitude(), pharmacy.getLongitude()
                    );
                    return toResWithDistance(pharmacy, distance);
                })
                .collect(Collectors.toList());
    }

    private PharmacyRes toResWithDistance(Pharmacy pharmacy, Double distance) {
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
                .distance(distance)
                .createdAt(pharmacy.getCreatedAt())
                .updatedAt(pharmacy.getUpdatedAt())
                .build();
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // 지구 반지름 (m)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    // 약국 단건 조회
    @Transactional(readOnly = true)
    public PharmacyRes getPharmacy(Long pharmacyId) {
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new PharmacyException(PharmacyErrorCode.PHARMACY_NOT_FOUND));
        return toRes(pharmacy);
    }

    // 24시간 약국 조회
    @Transactional(readOnly = true)
    public List<PharmacyRes> get24HourPharmacies() {
        return pharmacyRepository.findByTwentyFourHoursTrue()
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    // 심야 약국 조회
    @Transactional(readOnly = true)
    public List<PharmacyRes> getNightPharmacies() {
        return pharmacyRepository.findByNightPharmacyTrue()
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    // Entity → Response DTO 변환
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