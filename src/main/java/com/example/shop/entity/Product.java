package com.example.shop.entity;

import com.example.shop.model.ProductStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "name")
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "price")
    private Float price;

    @Column(name = "discount")
    private Float discount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
