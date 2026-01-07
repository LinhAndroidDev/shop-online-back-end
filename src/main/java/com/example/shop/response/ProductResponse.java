package com.example.shop.response;

import com.example.shop.model.ProductStatus;
import lombok.Data;

import java.util.List;

public class ProductResponse extends BaseResponse<ProductResponse.ProductData> {

    @Data
    public static class ProductData {
        private Long id;
        private CategoryResponse.CategoryData category;
        private String name;
        private String description;
        private String thumbnail;
        private Float price;
        private Float discount;
        private ProductStatus status;
        private String createdAt;
        private List<String> images;
        private ProductVariantData variant;
    }

    @Data
    public static class ProductVariantData {
        private Long id;
        private String origin;
        private List<String> size;
        private List<ProductColorData> color;
    }

    @Data
    public static class ProductColorData {
        private String name;
        private String hexCode;
    }
}
