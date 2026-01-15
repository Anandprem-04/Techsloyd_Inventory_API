package com.inventory.backend_api.dto;
import lombok.Data;

@Data
public class CreateOptionRequest {
    private String name;         // e.g., "Color"
    private String type;         // e.g., "DROPDOWN"
    private Integer position;
    private Boolean isRequired;
}