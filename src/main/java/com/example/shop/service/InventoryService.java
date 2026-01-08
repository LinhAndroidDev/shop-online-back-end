package com.example.shop.service;

import com.example.shop.dto.InventoryRequest;
import com.example.shop.entity.Inventory;
import com.example.shop.mapper.InventoryMapper;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.model.OutOfStock;
import com.example.shop.repository.InventoryRepository;
import com.example.shop.response.InventoryResponse;
import com.example.shop.response.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    InventoryMapper inventoryMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductMapper productMapper;

    public List<InventoryResponse.InventoryData> getInventories() {
        return inventoryRepository.findAll().stream().map((inventory) -> {
                    ProductResponse.ProductData product = productService.getProductById((long) inventory.getProductId());
                    return inventoryMapper.toResponse(inventory, product);
                }
        ).toList();
    }

    public HttpStatus updateInventoryQuantity(InventoryRequest request) {
        Inventory inventory = inventoryRepository.findById(request.getId()).orElse(null);
        if (inventory == null) {
            return HttpStatus.NOT_FOUND;
        }

        inventory.setQuantity(request.getQuantity());
        inventoryRepository.save(inventory);
        return HttpStatus.OK;
    }

    public List<OutOfStock> getOutOfStockProducts() {
        List<Inventory> inventories = inventoryRepository.findByQuantityLessThanEqual(0);
        return inventories.stream().map((inventory) -> {
            ProductResponse.ProductData product = productService.getProductById((long) inventory.getProductId());
            OutOfStock productOutOfStock = new OutOfStock();
            productOutOfStock.setId(inventory.getId());
            productOutOfStock.setName(product.getName());
            productOutOfStock.setQuantity(inventory.getQuantity());
            productOutOfStock.setUpdatedAt(inventory.getUpdatedAt().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            productOutOfStock.setNote("Đã hết hàng");
            return productOutOfStock;
        }).toList();
    }
}
