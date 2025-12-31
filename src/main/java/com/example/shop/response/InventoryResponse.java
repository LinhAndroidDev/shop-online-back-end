package com.example.shop.response;

import lombok.Data;

public class InventoryResponse extends BaseResponse<InventoryResponse.InventoryData> {

    @Data
    public static class InventoryData {
        private Long id;
        private ProductResponse.ProductData product;
        private Integer quantity;
        private String updatedAt;
    }
}
