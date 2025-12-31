package com.example.shop.controller;

import com.example.shop.dto.InventoryRequest;
import com.example.shop.response.BaseResponse;
import com.example.shop.response.InventoryResponse;
import com.example.shop.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController extends BaseController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<InventoryResponse.InventoryData>>> getInventories() {
        try {
            return successResponse(inventoryService.getInventories(), "Lấy danh sách tồn kho thành công");
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }

    @PutMapping
    public ResponseEntity<BaseResponse<List<InventoryResponse.InventoryData>>> updateInventoryQuantity(@RequestBody InventoryRequest request) {
        try {
            HttpStatus status = inventoryService.updateInventoryQuantity(request);
            if (status == HttpStatus.NOT_FOUND) {
                return errorResponse("Tồn kho không tồn tại", HttpStatus.NOT_FOUND);
            }
            return successResponse(inventoryService.getInventories(), "Cập nhật số lượng tồn kho thành công");
        } catch (HttpClientErrorException e) {
            return errorResponse(e.getMessage(), Objects.requireNonNull(HttpStatus.resolve(e.getStatusCode().value())));
        }
    }
}
