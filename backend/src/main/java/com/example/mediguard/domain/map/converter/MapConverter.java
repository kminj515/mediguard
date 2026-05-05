package com.example.mediguard.domain.map.converter;

import com.example.mediguard.domain.map.dto.res.PharmacyRes;
import com.example.mediguard.domain.map.dto.res.LocationRes;

import java.util.Map;

public class MapConverter {

    public static PharmacyRes toPharmacyRes(Map<String, Object> doc) {
        String placeName = (String) doc.get("place_name");
        String pharmacyName = extractPharmacyName(placeName);

        return PharmacyRes.builder()
                .id(Long.parseLong((String) doc.get("id")))
                .name(pharmacyName)
                .branchName(placeName.replace(pharmacyName, "").trim())
                .fullName(placeName)
                .address((String) doc.get("address_name"))
                .roadAddress((String) doc.get("road_address_name"))
                .lat(Double.parseDouble((String) doc.get("y")))
                .lon(Double.parseDouble((String) doc.get("x")))
                .phoneNumber((String) doc.get("phone"))
                .distance(toDouble(doc.get("distance")))
                .build();
    }

    private static String extractPharmacyName(String placeName) {
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

        for (String name : pharmacyNames) {
            if (placeName.contains(name)) return name;
        }

        return placeName.split(" ")[0];
    }

    private static Double toDouble(Object value) {
        if (value == null || value.toString().isEmpty()) return 0.0;
        return Double.parseDouble(value.toString());
    }

    // ✅ 공통 기능
    public static LocationRes toLocationResFromKeyword(Map<String, Object> doc) {
        return LocationRes.builder()
                .placeName((String) doc.get("place_name"))
                .address((String) doc.get("address_name"))
                .roadAddress((String) doc.get("road_address_name"))
                .latitude(toDouble(doc.get("y")))
                .longitude(toDouble(doc.get("x")))
                .build();
    }

    public static LocationRes toLocationResFromAddress(Map<String, Object> doc) {
        Map<String, Object> addressObj = (Map<String, Object>) doc.get("address");
        Map<String, Object> roadAddressObj = (Map<String, Object>) doc.get("road_address");

        String mainName = (roadAddressObj != null)
                ? (String) roadAddressObj.get("address_name")
                : (String) addressObj.get("address_name");

        return LocationRes.builder()
                .placeName(mainName)
                .address(addressObj != null ? (String) addressObj.get("address_name") : "")
                .roadAddress(roadAddressObj != null ? (String) roadAddressObj.get("address_name") : "")
                .latitude(toDouble(doc.get("y")))
                .longitude(toDouble(doc.get("x")))
                .build();
    }
}
