package com.example.shop.service;

import com.example.shop.repository.ProductColorRepository;
import com.example.shop.repository.ProductSizeRepository;
import com.example.shop.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private ProductColorRepository productColorRepository;


}
