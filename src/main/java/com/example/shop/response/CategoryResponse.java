package com.example.shop.response;

import lombok.Data;

public class CategoryResponse extends BaseResponse<CategoryResponse.CategoryData> {

    @Data
    public static class CategoryData {
        private Long id;
        private String name;
        private Long parentId;
        private String createdAt;
    }
}
