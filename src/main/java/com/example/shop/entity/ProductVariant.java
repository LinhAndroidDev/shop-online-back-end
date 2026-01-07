package com.example.shop.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_variant")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "origin")
    private String origin;

    @Column(name = "sizes")
    private String sizes;

    @Column(name = "colors")
    private String colors;
}
