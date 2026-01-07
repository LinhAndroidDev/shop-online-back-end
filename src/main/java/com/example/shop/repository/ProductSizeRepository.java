package com.example.shop.repository;

import com.example.shop.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {
    Optional<ProductSize> findByName(String name);
    List<ProductSize> findByProductVariantId(int productVariantId);
}
