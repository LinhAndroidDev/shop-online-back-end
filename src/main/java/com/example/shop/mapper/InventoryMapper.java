package com.example.shop.mapper;

import com.example.shop.entity.Inventory;
import com.example.shop.response.InventoryResponse;
import com.example.shop.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper extends BaseMapper {
    @Mapping(source = "inventory.id", target = "id")
    @Mapping(source = "product", target = "product")
    @Mapping(source = "inventory.updatedAt", target = "updatedAt", qualifiedByName = "dateToString")
    InventoryResponse.InventoryData toResponse(Inventory inventory, ProductResponse.ProductData product);
}
