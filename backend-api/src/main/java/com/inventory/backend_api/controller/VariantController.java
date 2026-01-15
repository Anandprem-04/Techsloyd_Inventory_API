package com.inventory.backend_api.controller;
import com.inventory.backend_api.dto.*;
import com.inventory.backend_api.entity.*;
import com.inventory.backend_api.services.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class VariantController {

    @Autowired
    private VariantService variantService;

    // 1. GET /api/variants/options
    @GetMapping("/variants/options")
    public List<VariantOption> getAllOptions() {
        return variantService.getAllOptions();
    }

    // 2. POST /api/variants/options
    @PostMapping("/variants/options")
    public VariantOption createOption(@RequestBody CreateOptionRequest req) {
        return variantService.createOption(req);
    }

    // 3. GET /api/variants/options/:id/values
    @GetMapping("/variants/options/{id}/values")
    public List<VariantOptionValue> getOptionValues(@PathVariable String id) {
        return variantService.getOptionValues(id);
    }

    // 4. POST /api/variants/options/:id/values
    @PostMapping("/variants/options/{id}/values")
    public VariantOptionValue createOptionValue(@PathVariable String id, @RequestBody CreateValueRequest req) {
        return variantService.createOptionValue(id, req);
    }

    // 5. GET /api/products/:productId/variants
    @GetMapping("/products/{productId}/variants")
    public List<ProductVariant> getProductVariants(@PathVariable String productId) {
        return variantService.getProductVariants(productId);
    }

    // 6. POST /api/variants/generate-matrix
    @PostMapping("/variants/generate-matrix")
    public List<ProductVariant> generateMatrix(@RequestBody GenerateMatrixRequest req) {
        return variantService.generateMatrix(req);
    }

    // 7. PUT /api/variants/:id/inventory
    @PutMapping("/variants/{id}/inventory")
    public ResponseEntity<ProductVariant> updateInventory(@PathVariable String id, @RequestBody Map<String, Integer> payload) {
        return ResponseEntity.ok(variantService.updateInventory(id, payload.get("stockLevel")));
    }

    // 8. PUT /api/variants/:id/pricing
    @PutMapping("/variants/{id}/pricing")
    public ResponseEntity<ProductVariant> updatePricing(@PathVariable String id, @RequestBody Map<String, BigDecimal> payload) {
        return ResponseEntity.ok(variantService.updatePricing(id, payload.get("price")));
    }

    // 9. DELETE /api/variants/options/:id
    @DeleteMapping("/variants/options/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable String id) {
        variantService.deleteOption(id);
        return ResponseEntity.ok().build();
    }

    // 10. DELETE /api/variants/options/:id/values/:valueId
    @DeleteMapping("/variants/options/{id}/values/{valueId}")
    public ResponseEntity<Void> deleteOptionValue(@PathVariable String id, @PathVariable String valueId) {
        variantService.deleteOptionValue(id, valueId);
        return ResponseEntity.ok().build();
    }
}