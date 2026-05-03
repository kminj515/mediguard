package com.example.mediguard.domain.quiz.repository;

import com.example.mediguard.domain.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}