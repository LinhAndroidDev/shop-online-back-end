package com.example.shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {
    private Long id;
    private int categoryId;
    private String name;
    private String description;
    private String thumbnail;
    private Float price;
    private Float discount;
    private String status;
    private List<String> images;
    private ProductVariantRequest variant;

    @Data
    public static class ProductVariantRequest {
        private String origin;
        private List<String> size;
        private List<ProductColorRequest> color;
    }

    @Data
    public static class ProductColorRequest {
        private String name;
        private String hexCode;
    }
}
