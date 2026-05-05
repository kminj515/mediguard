package com.example.mediguard.domain.pharmacy.repository;

import com.example.mediguard.domain.pharmacy.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    // 약국 이름으로 조회
    Optional<Pharmacy> findByName(String name);

    // 약국 이름 키워드 검색
    List<Pharmacy> findByNameContaining(String keyword);

    // 심야 약국만 조회
    List<Pharmacy> findByNightPharmacyTrue();

    // 24시간 약국만 조회
    List<Pharmacy> findByTwentyFourHoursTrue();

    // 심야 + 24시간 약국 복합 필터
    List<Pharmacy> findByNightPharmacyTrueAndTwentyFourHoursTrue();

    // Haversine 공식 기반 반경 내 약국 조회 (거리순 정렬)
    @Query(value = """
            SELECT *, (
                6371000 * acos(
                    cos(radians(:lat)) * cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:lon)) +
                    sin(radians(:lat)) * sin(radians(latitude))
                )
            ) AS distance
            FROM pharmacy
            HAVING distance <= :radius
            ORDER BY distance ASC
            """, nativeQuery = true)
    List<Pharmacy> findPharmaciesWithinRadius(
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("radius") Double radius
    );

    // Haversine 공식 기반 심야 약국만 반경 내 조회
    @Query(value = """
            SELECT *, (
                6371000 * acos(
                    cos(radians(:lat)) * cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:lon)) +
                    sin(radians(:lat)) * sin(radians(latitude))
                )
            ) AS distance
            FROM pharmacy
            WHERE is_night_pharmacy = true
            HAVING distance <= :radius
            ORDER BY distance ASC
            """, nativeQuery = true)
    List<Pharmacy> findNightPharmaciesWithinRadius(
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("radius") Double radius
    );

    // Haversine 공식 기반 24시간 약국만 반경 내 조회
    @Query(value = """
            SELECT *, (
                6371000 * acos(
                    cos(radians(:lat)) * cos(radians(latitude)) *
                    cos(radians(longitude) - radians(:lon)) +
                    sin(radians(:lat)) * sin(radians(latitude))
                )
            ) AS distance
            FROM pharmacy
            WHERE is_twenty_four_hours = true
            HAVING distance <= :radius
            ORDER BY distance ASC
            """, nativeQuery = true)
    List<Pharmacy> findTwentyFourPharmaciesWithinRadius(
            @Param("lat") Double latitude,
            @Param("lon") Double longitude,
            @Param("radius") Double radius
    );
}
