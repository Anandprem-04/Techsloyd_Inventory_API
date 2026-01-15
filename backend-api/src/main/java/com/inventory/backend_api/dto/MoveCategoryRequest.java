package com.inventory.backend_api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MoveCategoryRequest {
    private String categoryId;
    private String newParentId; // nullable -> move to root when null

}
