package com.example.shop.model;

import lombok.Data;

@Data
public class OutOfStock {
    private Long id;
    private String name;
    private int quantity;
    private String updatedAt;
    private String note;
}
