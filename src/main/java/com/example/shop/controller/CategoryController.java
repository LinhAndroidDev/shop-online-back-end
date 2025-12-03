package com.example.shop.controller;

import com.example.shop.dto.CategoryRequest;
import com.example.shop.response.BaseResponse;
import com.example.shop.response.CategoryResponse;
import com.example.shop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/category")
public class CategoryController extends BaseController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<CategoryResponse.CategoryData>>> getCategories() {
        try {
            return successResponse(categoryService.getCategories(), null);
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }

    @PostMapping
    public ResponseEntity<BaseResponse<List<CategoryResponse.CategoryData>>> createCategory(@RequestBody CategoryRequest request) {
        try {
            HttpStatus status = categoryService.createCategory(request);
            if (status == HttpStatus.CONFLICT) {
                return errorResponse("Tên danh mục đã được sử dụng", HttpStatus.CONFLICT);
            }
            return successResponse(categoryService.getCategories(), "Danh mục đã được tạo thành công");
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }

    @PutMapping
    public ResponseEntity<BaseResponse<List<CategoryResponse.CategoryData>>> updateCategory(@RequestBody CategoryRequest request) {
        try {
            HttpStatus status = categoryService.updateCategory(request);
            if (status == HttpStatus.CONFLICT) {
                return errorResponse("Tên danh mục đã được sử dụng", HttpStatus.CONFLICT);
            } else if (status == HttpStatus.NOT_FOUND) {
                return errorResponse("Danh mục không tồn tại", HttpStatus.NOT_FOUND);
            }
            return successResponse(categoryService.getCategories(), "Danh mục đã được cập nhật thành công");
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<List<CategoryResponse.CategoryData>>> deleteCategory(@PathVariable Long id) {
        try {
            HttpStatus status = categoryService.deleteCategory(id);
            if (status == HttpStatus.NOT_FOUND) {
                return errorResponse("Danh mục không tồn tại", HttpStatus.NOT_FOUND);
            }
            return successResponse(categoryService.getCategories(), "Danh mục đã được xóa thành công");
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }
}
