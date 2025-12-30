package com.example.shop.service;

import com.example.shop.dto.ProductRequest;
import com.example.shop.entity.Category;
import com.example.shop.entity.Inventory;
import com.example.shop.entity.Product;
import com.example.shop.mapper.CategoryMapper;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.InventoryRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.response.CategoryResponse;
import com.example.shop.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    public List<ProductResponse.ProductData> getProducts() {
        return productRepository.findAll().stream().map(product -> {
            Category category = categoryRepository.findById((long) product.getCategoryId()).orElse(null);
            CategoryResponse.CategoryData categoryData = categoryMapper.toResponse(category);
            return productMapper.toResponse(product, categoryData);
        }).toList();
    }

    public HttpStatus createProduct(ProductRequest request) {
        if (request.getName().isEmpty()) {
            return HttpStatus.BAD_REQUEST;
        }

        if (productRepository.existsByName(request.getName())) {
            return HttpStatus.CONFLICT;
        }

        Product savedProduct = productRepository.save(productMapper.toEntity(request));
        saveInventoryForProduct(savedProduct);
        return HttpStatus.OK;
    }

    public HttpStatus updateProduct(ProductRequest request) {
        if (productRepository.existsByName(request.getName())) {
            return HttpStatus.CONFLICT;
        }

        var product = productRepository.findById(request.getId()).orElse(null);
        if (product == null) {
            return HttpStatus.NOT_FOUND;
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategoryId(request.getCategoryId());

        Product savedProduct = productRepository.save(product);
        saveInventoryForProduct(savedProduct);
        return HttpStatus.OK;
    }

    public HttpStatus deleteProduct(Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return HttpStatus.NOT_FOUND;
        }

        Inventory inventory = inventoryRepository.findByProductId(product.getId());
        if (inventory != null) {
            inventoryRepository.delete(inventory);
        }
        productRepository.delete(product);
        return HttpStatus.OK;
    }

    private void saveInventoryForProduct(Product product) {
        Inventory inventory = new Inventory();
        inventory.setProductId(product.getId().intValue());
        inventory.setQuantity(0);
        inventoryRepository.save(inventory);
    }
}
