package com.example.shop.service;

import com.example.shop.dto.ProductRequest;
import com.example.shop.entity.Category;
import com.example.shop.entity.Inventory;
import com.example.shop.entity.Product;
import com.example.shop.entity.ProductImage;
import com.example.shop.mapper.CategoryMapper;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.repository.InventoryRepository;
import com.example.shop.repository.ProductImageRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.response.CategoryResponse;
import com.example.shop.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    private ProductImageRepository productImageRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductImageService productImageService;

    @Autowired
    private ProductVariantService productVariantService;

    public List<ProductResponse.ProductData> getProducts() {
        return productRepository.findAll().stream().map(product -> {
            Category category = categoryRepository.findById((long) product.getCategoryId()).orElse(null);
            CategoryResponse.CategoryData categoryData = categoryMapper.toResponse(category);
            List<ProductImage> productImageList = productImageRepository.findByProductId(product.getId());
            List<String> images = productImageList.stream()
                    .map(ProductImage::getUrl)
                    .toList();
            return productMapper.toResponse(product, categoryData, images);
        }).toList();
    }

    public ProductResponse.ProductData getProductById(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        Category category = categoryRepository.findById((long) product.getCategoryId()).orElse(null);
        CategoryResponse.CategoryData categoryData = categoryMapper.toResponse(category);
        List<ProductImage> productImageList = productImageRepository.findByProductId(product.getId());
        List<String> images = productImageList.stream()
                .map(ProductImage::getUrl)
                .toList();
        return productMapper.toResponse(product, categoryData, images);
    }

    public HttpStatus createProduct(ProductRequest request) {
        if (request.getName().isEmpty()) {
            return HttpStatus.BAD_REQUEST;
        }

        if (productRepository.existsByName(request.getName())) {
            return HttpStatus.CONFLICT;
        }

        Product savedProduct = productRepository.save(productMapper.toEntity(request));
        request.getImages().forEach((e) -> {
            ProductImage productImage = new ProductImage();
            productImage.setProductId(savedProduct.getId().intValue());
            productImage.setUrl(e);
            productImageRepository.save(productImage);
        });
        saveInventoryForProduct(savedProduct);
        return HttpStatus.OK;
    }

    public HttpStatus updateProduct(ProductRequest request) {
        var product = productRepository.findById(request.getId()).orElse(null);
        if (product == null) {
            return HttpStatus.NOT_FOUND;
        }

        if (!Objects.equals(product.getName(), request.getName()) && productRepository.existsByName(request.getName())) {
            return HttpStatus.CONFLICT;
        }

        Product savedProduct = productRepository.save(productMapper.toEntity(request));
        deleteProductImages(savedProduct.getId());
        request.getImages().forEach((e) -> {
            ProductImage productImage = new ProductImage();
            productImage.setProductId(savedProduct.getId().intValue());
            productImage.setUrl(e);
            productImageRepository.save(productImage);
        });
        return HttpStatus.OK;
    }

    private void deleteProductImages(Long productId) {
        List<ProductImage> oldImages = productImageRepository.findByProductId(productId);
        productImageRepository.deleteAll(oldImages);
        productImageService.deleteImagesAsync(oldImages);
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
