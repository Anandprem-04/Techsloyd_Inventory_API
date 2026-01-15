package com.inventory.backend_api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryStatsResponse {
    private long totalCategories;
    private long activeCategories;
    private long inactiveCategories;
    private long rootCategories; // Number of top-level parents
}