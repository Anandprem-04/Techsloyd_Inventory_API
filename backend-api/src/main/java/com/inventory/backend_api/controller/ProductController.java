package com.inventory.backend_api.controller;

import com.inventory.backend_api.entity.Product;
import com.inventory.backend_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // 1. GET /api/products (Pagination supported)
    @GetMapping
    public Page<Product> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }

    // 2. GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. POST /api/products
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // 4. PUT /api/products/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody Product details) {
        return productRepository.findById(id).map(existing -> {
            // Update fields if provided
            if (details.getName() != null) existing.setName(details.getName());
            if (details.getSku() != null) existing.setSku(details.getSku());
            if (details.getPrice() != null) existing.setPrice(details.getPrice());
            if (details.getStockLevel() != null) existing.setStockLevel(details.getStockLevel());
            if (details.getDescription() != null) existing.setDescription(details.getDescription());
            if (details.getStatus() != null) existing.setStatus(details.getStatus());
            if (details.getCategory() != null) existing.setCategory(details.getCategory());

            existing.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(productRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 6. GET /api/products/search?query=shirt
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String query) {
        return productRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(query, query);
    }

    // 7. GET /api/products/by-category/{categoryId}
    @GetMapping("/by-category/{categoryId}")
    public Page<Product> getProductsByCategoryOrParent(
            @PathVariable String categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return productRepository.findByCategoryOrParentId(categoryId, PageRequest.of(page, size));
    }
    // 8. POST /api/products/bulk-update
    @PostMapping("/bulk-update")
    public ResponseEntity<List<Product>> bulkUpdateProducts(@RequestBody List<Product> products) {
        List<Product> updatedProducts = new ArrayList<>();

        for (Product updateReq : products) {
            if (updateReq.getId() != null) {
                Optional<Product> optionalProduct = productRepository.findById(updateReq.getId());

                if (optionalProduct.isPresent()) {
                    Product existing = optionalProduct.get();

                    // Apply partial updates
                    if (updateReq.getName() != null) existing.setName(updateReq.getName());
                    if (updateReq.getSku() != null) existing.setSku(updateReq.getSku());
                    if (updateReq.getPrice() != null) existing.setPrice(updateReq.getPrice());
                    if (updateReq.getStockLevel() != null) existing.setStockLevel(updateReq.getStockLevel());
                    if (updateReq.getDescription() != null) existing.setDescription(updateReq.getDescription());
                    if (updateReq.getStatus() != null) existing.setStatus(updateReq.getStatus());
                    if (updateReq.getCategory() != null) existing.setCategory(updateReq.getCategory());

                    existing.setUpdatedAt(LocalDateTime.now());
                    updatedProducts.add(existing);
                }
            }
        }

        // Save all changes in one batch for efficiency
        List<Product> saved = productRepository.saveAll(updatedProducts);
        return ResponseEntity.ok(saved);
    }

    // 9. POST /api/products/bulk-delete
    // Expects a simple list of String IDs: ["id1", "id2", "id3"]
    @PostMapping("/bulk-delete")
    public ResponseEntity<Void> bulkDeleteProducts(@RequestBody List<String> ids) {
        if (ids != null && !ids.isEmpty()) {
            productRepository.deleteAllById(ids);
        }
        return ResponseEntity.ok().build();
    }
}