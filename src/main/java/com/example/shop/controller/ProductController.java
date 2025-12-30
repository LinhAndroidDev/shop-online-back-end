package com.example.shop.controller;

import com.example.shop.dto.ProductRequest;
import com.example.shop.response.BaseResponse;
import com.example.shop.response.ProductResponse;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @GetMapping
    ResponseEntity<BaseResponse<List<ProductResponse.ProductData>>> getProducts() {
        try {
            return successResponse(productService.getProducts(), null);
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }

    @PostMapping
    ResponseEntity<BaseResponse<List<ProductResponse.ProductData>>> createProduct(@RequestBody ProductRequest request) {
        try {
            HttpStatus status = productService.createProduct(request);
            return switch (status) {
                case BAD_REQUEST -> errorResponse("Tên sản phẩm ko được để trống", HttpStatus.BAD_REQUEST);
                case CONFLICT -> errorResponse("Tên sản phẩm đã được sử dụng", HttpStatus.CONFLICT);
                default -> successResponse(productService.getProducts(), "Sản phẩm đã được tạo thành công");
            };
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }

    @PutMapping
    ResponseEntity<BaseResponse<List<ProductResponse.ProductData>>> updateProduct(@RequestBody ProductRequest request) {
        try {
            HttpStatus status = productService.updateProduct(request);
            if (status == HttpStatus.CONFLICT) {
                return errorResponse("Tên sản phẩm đã được sử dụng", HttpStatus.CONFLICT);
            } else if (status == HttpStatus.NOT_FOUND) {
                return errorResponse("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND);
            }
            return successResponse(productService.getProducts(), "Sản phẩm đã được cập nhật thành công");
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<BaseResponse<List<ProductResponse.ProductData>>> deleteProduct(@PathVariable Long id) {
        try {
            HttpStatus status = productService.deleteProduct(id);
            if (status == HttpStatus.NOT_FOUND) {
                return errorResponse("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND);
            }
            return successResponse(productService.getProducts(), "Sản phẩm đã được xóa thành công");
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }
}
