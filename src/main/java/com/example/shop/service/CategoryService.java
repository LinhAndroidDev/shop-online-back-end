package com.example.shop.service;

import com.example.shop.dto.CategoryRequest;
import com.example.shop.entity.Category;
import com.example.shop.repository.CategoryRepository;
import com.example.shop.response.CategoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryResponse.CategoryData> getCategories() {
        return categoryRepository.findAll().stream().map(category -> {
            CategoryResponse.CategoryData data = new CategoryResponse.CategoryData();
            data.setId(category.getId());
            data.setName(category.getName());
            data.setParentId(category.getParentId());
            data.setCreatedAt(category.getCreatedAt().toString());
            return data;
        }).toList();
    }

    public HttpStatus createCategory(CategoryRequest request) {
        boolean exists = categoryRepository.existsByName(request.getName()).orElse(false);
        if (exists) {
            return HttpStatus.CONFLICT;
        }
        Category category = new Category();
        category.setName(request.getName());
        category.setParentId(null);
        categoryRepository.save(category);
        return HttpStatus.OK;
    }

    public HttpStatus updateCategory(CategoryRequest request) {
        boolean exists = categoryRepository.existsByName(request.getName()).orElse(false);
        if (exists) {
            return HttpStatus.CONFLICT;
        }
        Category category = categoryRepository.findById(request.getId()).orElse(null);
        if (category == null) {
            return HttpStatus.NOT_FOUND;
        }
        category.setName(request.getName());
        categoryRepository.save(category);
        return HttpStatus.OK;
    }

    public HttpStatus deleteCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return HttpStatus.NOT_FOUND;
        }
        categoryRepository.delete(category);
        return HttpStatus.OK;
    }
}
