package com.inventory.backend_api.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CreateValueRequest {
    private String value;        // e.g., "Red"
    private String displayValue; // e.g., "#FF0000" or "🔴"
    private BigDecimal priceAdjustment; // e.g., +2.00 for XL size
}