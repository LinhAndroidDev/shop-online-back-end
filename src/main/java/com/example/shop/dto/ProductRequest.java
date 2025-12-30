package com.example.shop.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private Long id;
    private int categoryId;
    private String name;
    private String description;
    private String thumbnail;
    private Float price;
    private String status;
}
