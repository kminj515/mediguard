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
import java.net.URI;
import java.net.URLEncoder;
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

    // 공공API 호출 → 전국 데이터 DB 저장
    @Transactional
    public void fetchAndSavePharmacies() {
        int pageNo = 1;
        int numOfRows = 5000; // 전국 데이터를 빠르게 가져오기 위해 최대치 설정
        boolean hasMoreData = true;

        while (hasMoreData) {
            try {
                // 1. 전국 약국 FullData 내려받기 오퍼레이션으로 변경 (h 누락 오타 반영) [cite: 7, 14]
                String urlString = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyFullDown"
                        + "?serviceKey=" + apiKey
                        + "&pageNo=" + pageNo
                        + "&numOfRows=" + numOfRows
                        + "&_type=xml";

                log.info("🚀 API 호출 주소: {}", urlString);

                // 2. 이중 인코딩 방지를 위한 URI 객체 사용
                java.net.URI uri = new java.net.URI(urlString);
                String response = restTemplate.getForObject(uri, String.class);

                if (response == null || response.isEmpty()) {
                    log.error("❌ API 응답이 비어있습니다.");
                    throw new PharmacyException(PharmacyErrorCode.PHARMACY_NOT_FOUND);
                }

                log.info("📝 API 응답 수신: {}", response.substring(0, Math.min(response.length(), 200)));

                // 3. 파싱 및 저장
                List<Pharmacy> pharmacies = parseXmlToPharmacies(response);

                if (pharmacies.isEmpty()) {
                    hasMoreData = false; // 더 이상 가져올 데이터가 없음
                    log.warn("⚠️ 파싱된 약국 정보가 없습니다. 응답 내용을 확인하세요.");
                } else {
                    pharmacyRepository.saveAll(pharmacies);
                    log.info("✅ 약국 정보 {}건 저장 완료", pharmacies.size());
                    pageNo++; // 다음 페이지로 이동
                }

            } catch (PharmacyException e) {
                throw e;
            } catch (Exception e) {
                log.error("❌ 약국 API 처리 중 예외 발생: {}", e.getMessage(), e);
                hasMoreData = false; // 에러 발생 시 루프 종료
            }
        }
    }

    private List<Pharmacy> parseXmlToPharmacies(String xml) throws Exception {
        if (xml.trim().startsWith("{")) {
            log.error("❌ 서버가 XML이 아닌 JSON을 반환했습니다.");
            return new ArrayList<>();
        }

        List<Pharmacy> pharmacies = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        NodeList items = doc.getElementsByTagName("item");
        for (int i = 0; i < items.getLength(); i++) {
            Element item = (Element) items.item(i);
            String name = getTagValue("dutyName", item);
            String address = getTagValue("dutyAddr", item);
            String contact = getTagValue("dutyTel1", item);
            String latStr = getTagValue("wgs84Lat", item);
            String lonStr = getTagValue("wgs84Lon", item);

            if (name == null || latStr == null || lonStr == null) continue;

            // ✅ 영업시간 데이터 추출 및 판별 로직 추가
            String startTime = getTagValue("dutyTime1s", item);
            String endTime = getTagValue("dutyTime1c", item);

            // 24시간 여부 판별 (0000시작, 2400종료 기준)
            boolean is24Hours = "0000".equals(startTime) && "2400".equals(endTime);

            // 심야 약국 여부 판별 (월~토 중 종료 시간이 22:00 이상인 경우)
            boolean isNight = false;
            for (int d = 1; d <= 6; d++) {
                String closeTime = getTagValue("dutyTime" + d + "c", item);
                if (closeTime != null && Integer.parseInt(closeTime) >= 2200) {
                    isNight = true;
                    break;
                }
            }

            pharmacies.add(Pharmacy.builder()
                    .name(name)
                    .address(address)
                    .contact(contact)
                    .latitude(Double.parseDouble(latStr))
                    .longitude(Double.parseDouble(lonStr))
                    .operatingHours(startTime != null ? startTime + " - " + endTime : "정보 없음")
                    .nightPharmacy(isNight)
                    .twentyFourHours(is24Hours)
                    .build());
        }
        return pharmacies;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) return null;
        return nodeList.item(0).getTextContent();
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
        PharmacyRes res = toRes(pharmacy);
        res.setDistance(distance);
        return res;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
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
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PharmacyRes> getNightPharmacies() {
        return pharmacyRepository.findByNightPharmacyTrue()
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
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