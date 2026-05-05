package com.example.mediguard.domain.diagnosis.repository;

import com.example.mediguard.domain.diagnosis.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    // 약 이름으로 검색 (정확히 일치)
    Optional<Medicine> findByName(String name);

    // 약 이름 키워드 검색 (LIKE)
    List<Medicine> findByNameContaining(String keyword);

    // 카테고리별 약 목록 조회
    List<Medicine> findByCategory(String category);

    // 공복 복용 가능한 약 목록 조회
    List<Medicine> findByEmptyStomachSafeTrue();

    // 졸림 유발 없는 약 목록 조회
    List<Medicine> findByDrowsinessFalse();

    // 카테고리 + 공복 복용 가능 여부 복합 조회
    List<Medicine> findByCategoryAndEmptyStomachSafe(String category, boolean emptyStomachSafe);

    // 주요 성분으로 검색
    @Query("SELECT m FROM Medicine m WHERE m.activeIngredient LIKE %:ingredient%")
    List<Medicine> findByActiveIngredientContaining(@Param("ingredient") String ingredient);

    // 효능 키워드로 검색
    @Query("SELECT m FROM Medicine m WHERE m.efficacy LIKE %:keyword%")
    List<Medicine> findByEfficacyContaining(@Param("keyword") String keyword);
}
