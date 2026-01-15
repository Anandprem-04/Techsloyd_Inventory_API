package com.inventory.backend_api.dto;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class ScanResponse {
    private boolean found;
    private String barcode;
    private String type; // "PRODUCT" or "VARIANT"

    // Unified Fields (So UI can just display these)
    private String name;        // Product Name
    private String description;
    private String sku;
    private BigDecimal price;
    private Integer stockLevel;

    // IDs for further actions
    private String productId;
    private String variantId;
}