package com.example.mediguard.domain.map.converter;

import com.example.mediguard.domain.pharmacy.dto.res.PharmacyRes;
import com.example.mediguard.domain.map.dto.res.LocationRes;

import java.util.Map;

public class MapConverter {

    public static PharmacyRes toPharmacyRes(Map<String, Object> doc) {
        String placeName = (String) doc.get("place_name");

        return PharmacyRes.builder()
                .pharmacyId(Long.parseLong((String) doc.get("id")))
                .name(placeName)                                        // ✅ fullName 제거 → name에 통합
                .address((String) doc.get("address_name"))             // ✅ address
                .latitude(toDouble(doc.get("y")))                      // ✅ lat → latitude
                .longitude(toDouble(doc.get("x")))                     // ✅ lon → longitude
                .contact((String) doc.get("phone"))                    // ✅ phoneNumber → contact
                .distance(toDouble(doc.get("distance")))               // ✅ distance
                .nightPharmacy(false)                                  // ✅ 기본값
                .twentyFourHours(false)                                // ✅ 기본값
                .build();
    }

    private static Double toDouble(Object value) {
        if (value == null || value.toString().isEmpty()) return 0.0;
        return Double.parseDouble(value.toString());
    }

    // 공통 기능
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
