package com.example.shop.mapper;

import com.example.shop.dto.CategoryRequest;
import com.example.shop.entity.Category;
import com.example.shop.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper {
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Category toEntity(CategoryRequest request);

    @Mapping(source = "category.createdAt", target = "createdAt", qualifiedByName = "dateToString")
    CategoryResponse.CategoryData toResponse(Category category);

}
