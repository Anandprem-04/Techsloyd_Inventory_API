package com.inventory.backend_api.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class GenerateMatrixRequest {
    private String productId;
    private List<OptionSelection> selections;
    private BigDecimal defaultPrice;
    private Integer defaultStock;

    @Data
    public static class OptionSelection {
        private String optionId;      // UUID of the Option (e.g. Color)
        private List<String> valueIds; // UUIDs of the Values (e.g. Red, Blue)
    }
}