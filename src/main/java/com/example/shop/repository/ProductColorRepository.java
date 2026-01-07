package com.example.shop.repository;

import com.example.shop.entity.ProductColor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductColorRepository extends JpaRepository<ProductColor, Long> {
    Optional<ProductColor> findByNameAndHexCode(String name, String hexCode);
    List<ProductColor> findByProductVariantId(int productVariantId);
}
