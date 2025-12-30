package com.example.shop.mapper;

import com.example.shop.dto.ProductRequest;
import com.example.shop.entity.Product;
import com.example.shop.response.CategoryResponse;
import com.example.shop.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper {
    @Mapping(target = "createdAt", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.createdAt", target = "createdAt", qualifiedByName = "dateToString")
    @Mapping(source = "category", target = "category")
    ProductResponse.ProductData toResponse(Product product, CategoryResponse.CategoryData category);

}
