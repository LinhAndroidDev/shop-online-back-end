package com.example.shop.response;

import com.example.shop.model.ProductStatus;
import lombok.Data;

public class ProductResponse extends BaseResponse<ProductResponse.ProductData> {

    @Data
    public static class ProductData {
        private Long id;
        private CategoryResponse.CategoryData category;
        private String name;
        private String description;
        private String thumbnail;
        private Float price;
        private ProductStatus status;
        private String createdAt;
    }
}
