package com.inventory.backend_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class CategoryReorderRequest {
    private List<ReorderItem> items;

    @Data
    public static class ReorderItem {
        private String id;
        private Integer position;
    }
}