package com.example.shop.service;

import com.example.shop.dto.ProductRequest;
import com.example.shop.entity.ProductColor;
import com.example.shop.entity.ProductSize;
import com.example.shop.entity.ProductVariant;
import com.example.shop.repository.ProductColorRepository;
import com.example.shop.repository.ProductSizeRepository;
import com.example.shop.repository.ProductVariantRepository;
import com.example.shop.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantService {
    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private ProductColorRepository productColorRepository;

    /**
     * Tạo ProductVariant từ ProductVariantRequest
     * @param productId ID của sản phẩm
     * @param variantRequest Request chứa thông tin variant
     */
    public void createProductVariant(Long productId, ProductRequest.ProductVariantRequest variantRequest) {
        if (variantRequest == null) {
            return;
        }

        // Tạo ProductVariant
        ProductVariant productVariant = new ProductVariant();
        productVariant.setProductId(productId.intValue());
        productVariant.setOrigin(variantRequest.getOrigin());
        productVariant.setSizes(""); // Tạm thời để trống, sẽ cập nhật sau
        productVariant.setColors(""); // Tạm thời để trống, sẽ cập nhật sau
        
        // Lưu để lấy id
        ProductVariant savedVariant = productVariantRepository.save(productVariant);
        int productVariantId = savedVariant.getId().intValue();

        // Tạo sizes và colors với productVariantId
        List<Long> sizeIds = createSizes(variantRequest.getSize(), productVariantId);
        List<Long> colorIds = createColors(variantRequest.getColor(), productVariantId);

        // Cập nhật lại chuỗi sizes và colors
        savedVariant.setSizes(sizeIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")));
        savedVariant.setColors(colorIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")));
        
        productVariantRepository.save(savedVariant);
    }

    /**
     * Cập nhật ProductVariant từ ProductVariantRequest
     * @param productId ID của sản phẩm
     * @param variantRequest Request chứa thông tin variant (có id)
     */
    public void updateProductVariant(Long productId, ProductRequest.ProductVariantRequest variantRequest) {
        if (variantRequest == null) {
            return;
        }

        // Nếu không có id, tạo mới variant
        if (variantRequest.getId() == null) {
            createProductVariant(productId, variantRequest);
            return;
        }

        int productVariantId = variantRequest.getId().intValue();

        // Xóa tất cả sizes và colors cũ có productVariantId trùng với id của variant
        deleteSizesByProductVariantId(productVariantId);
        deleteColorsByProductVariantId(productVariantId);

        // Tìm variant hiện tại
        ProductVariant productVariant = productVariantRepository.findById(variantRequest.getId())
                .orElse(null);
        
        // Nếu variant không tồn tại, tạo mới
        if (productVariant == null) {
            createProductVariant(productId, variantRequest);
            return;
        }

        // Cập nhật thông tin variant
        productVariant.setProductId(productId.intValue());
        productVariant.setOrigin(variantRequest.getOrigin());

        // Tạo mới sizes và colors với productVariantId
        List<Long> sizeIds = createSizes(variantRequest.getSize(), productVariantId);
        List<Long> colorIds = createColors(variantRequest.getColor(), productVariantId);

        // Cập nhật chuỗi sizes và colors
        productVariant.setSizes(sizeIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")));
        productVariant.setColors(colorIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", ")));
        
        productVariantRepository.save(productVariant);
    }

    /**
     * Tạo danh sách ProductSize từ danh sách tên size
     * @param sizeNames Danh sách tên size
     * @param productVariantId ID của ProductVariant
     * @return Danh sách id của các size đã tạo
     */
    private List<Long> createSizes(List<String> sizeNames, int productVariantId) {
        List<Long> sizeIds = new ArrayList<>();
        
        if (sizeNames == null || sizeNames.isEmpty()) {
            return sizeIds;
        }

        for (String sizeName : sizeNames) {
            ProductSize productSize = new ProductSize();
            productSize.setName(sizeName);
            productSize.setProductVariantId(productVariantId);
            ProductSize savedSize = productSizeRepository.save(productSize);
            sizeIds.add(savedSize.getId());
        }

        return sizeIds;
    }

    /**
     * Tạo danh sách ProductColor từ danh sách ProductColorRequest
     * @param colorRequests Danh sách ProductColorRequest
     * @param productVariantId ID của ProductVariant
     * @return Danh sách id của các color đã tạo
     */
    private List<Long> createColors(List<ProductRequest.ProductColorRequest> colorRequests, int productVariantId) {
        List<Long> colorIds = new ArrayList<>();
        
        if (colorRequests == null || colorRequests.isEmpty()) {
            return colorIds;
        }

        for (ProductRequest.ProductColorRequest colorRequest : colorRequests) {
            ProductColor productColor = new ProductColor();
            productColor.setName(colorRequest.getName());
            productColor.setHexCode(colorRequest.getHexCode());
            productColor.setProductVariantId(productVariantId);
            ProductColor savedColor = productColorRepository.save(productColor);
            colorIds.add(savedColor.getId());
        }

        return colorIds;
    }

    /**
     * Xóa tất cả ProductSize có productVariantId trùng với id
     * @param productVariantId ID của ProductVariant
     */
    private void deleteSizesByProductVariantId(int productVariantId) {
        List<ProductSize> sizes = productSizeRepository.findByProductVariantId(productVariantId);
        if (!sizes.isEmpty()) {
            productSizeRepository.deleteAll(sizes);
        }
    }

    /**
     * Xóa tất cả ProductColor có productVariantId trùng với id
     * @param productVariantId ID của ProductVariant
     */
    private void deleteColorsByProductVariantId(int productVariantId) {
        List<ProductColor> colors = productColorRepository.findByProductVariantId(productVariantId);
        if (!colors.isEmpty()) {
            productColorRepository.deleteAll(colors);
        }
    }

    /**
     * Lấy ProductVariantData từ productId
     * @param productId ID của sản phẩm
     * @return ProductVariantData hoặc null nếu không tìm thấy
     */
    public ProductResponse.ProductVariantData getVariantDataByProductId(Long productId) {
        ProductVariant productVariant = productVariantRepository.findByProductId(productId.intValue())
                .orElse(null);
        
        if (productVariant == null) {
            return null;
        }

        ProductResponse.ProductVariantData variantData = new ProductResponse.ProductVariantData();
        variantData.setId(productVariant.getId());
        variantData.setOrigin(productVariant.getOrigin());

        // Lấy danh sách sizes
        List<ProductSize> sizes = productSizeRepository.findByProductVariantId(productVariant.getId().intValue());
        List<String> sizeNames = sizes.stream()
                .map(ProductSize::getName)
                .collect(Collectors.toList());
        variantData.setSize(sizeNames);

        // Lấy danh sách colors
        List<ProductColor> colors = productColorRepository.findByProductVariantId(productVariant.getId().intValue());
        List<ProductResponse.ProductColorData> colorDataList = colors.stream()
                .map(color -> {
                    ProductResponse.ProductColorData colorData = new ProductResponse.ProductColorData();
                    colorData.setName(color.getName());
                    colorData.setHexCode(color.getHexCode());
                    return colorData;
                })
                .collect(Collectors.toList());
        variantData.setColor(colorDataList);

        return variantData;
    }

    /**
     * Xóa ProductVariant và tất cả sizes, colors liên quan theo productId
     * @param productId ID của sản phẩm
     */
    public void deleteProductVariantByProductId(Long productId) {
        // Tìm ProductVariant theo productId
        ProductVariant productVariant = productVariantRepository.findByProductId(productId.intValue())
                .orElse(null);
        
        // Nếu không tìm thấy variant, không cần xóa gì
        if (productVariant == null) {
            return;
        }

        int productVariantId = productVariant.getId().intValue();

        // Xóa tất cả sizes có productVariantId trùng với id của variant
        deleteSizesByProductVariantId(productVariantId);

        // Xóa tất cả colors có productVariantId trùng với id của variant
        deleteColorsByProductVariantId(productVariantId);

        // Xóa ProductVariant
        productVariantRepository.delete(productVariant);
    }
}
