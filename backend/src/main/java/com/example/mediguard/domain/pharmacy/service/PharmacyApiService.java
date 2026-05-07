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

    // 공공API 호출 → DB 저장
    @Transactional
    public void fetchAndSavePharmacies(String city, String district) {
        try {
            // 1. URL 조립 (가장 중요: &_type=xml 추가)
            // 브라우저와 동일한 결과를 내기 위해 &_type=xml을 명시적으로 붙입니다.
            String urlString = "http://apis.data.go.kr/B552657/ErmctInsttInfoInqireService/getParmacyListInfoInqire"
                    + "?serviceKey=" + apiKey
                    + "&Q0=" + URLEncoder.encode(city, StandardCharsets.UTF_8)
                    + "&Q1=" + URLEncoder.encode(district, StandardCharsets.UTF_8)
                    + "&ORD=NAME"
                    + "&pageNo=1"
                    + "&numOfRows=100"
                    + "&_type=xml"; // 서버가 JSON을 주지 못하도록 강제

            log.info("🚀 API 호출 주소: {}", urlString);

            // 2. 호출 (String으로 받아서 내용 확인)
            String response = restTemplate.getForObject(urlString, String.class);

            if (response == null || response.isEmpty()) {
                log.error("❌ API 응답이 비어있습니다.");
                throw new PharmacyException(PharmacyErrorCode.PHARMACY_NOT_FOUND);
            }

            // JSON이 왔는지 XML이 왔는지 로그로 확인
            log.info("📝 API 응답 수신: {}", response.substring(0, Math.min(response.length(), 200)));

            // 3. 파싱 및 저장
            List<Pharmacy> pharmacies = parseXmlToPharmacies(response);

            if (pharmacies.isEmpty()) {
                log.warn("⚠️ 파싱된 약국 정보가 없습니다. 응답 내용을 확인하세요.");
                throw new PharmacyException(PharmacyErrorCode.PHARMACY_NOT_FOUND);
            }

            pharmacyRepository.saveAll(pharmacies);
            log.info("✅ 약국 정보 {}건 저장 완료", pharmacies.size());

        } catch (PharmacyException e) {
            throw e;
        } catch (Exception e) {
            log.error("❌ 약국 API 처리 중 예외 발생: {}", e.getMessage(), e);
            throw new PharmacyException(PharmacyErrorCode.PHARMACY_NOT_FOUND);
        }
    }

    // XML 파싱
    private List<Pharmacy> parseXmlToPharmacies(String xml) throws Exception {
        // 응답이 JSON 형식이면 XML 파서가 돌아가지 않으므로 체크
        if (xml.trim().startsWith("{")) {
            log.error("❌ 서버가 XML이 아닌 JSON을 반환했습니다: {}", xml);
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