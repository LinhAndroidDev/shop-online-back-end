package com.example.shop.repository;

import com.example.shop.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Boolean> existsByName(String name);
}
