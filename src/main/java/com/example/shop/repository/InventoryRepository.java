package com.example.shop.repository;

import com.example.shop.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findByProductId(Long productId);

    List<Inventory> findByQuantityLessThanEqual(int threshold);
}
