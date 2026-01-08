package com.example.shop.model;

import lombok.Getter;

@Getter
public enum EmailType {
    OUT_OF_STOCK("Báo cáo sản phẩm sắp hết hàng"),
    INTO_WAREHOUSE("Thông báo nhập kho"),
    RELEASE_WAREHOUSE("Thông báo xuất kho");

    private final String value;

    EmailType(String value) {
        this.value = value;
    }

}
