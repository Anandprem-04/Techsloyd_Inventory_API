package com.inventory.backend_api.controller;

import com.inventory.backend_api.entity.Category;
import com.inventory.backend_api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*") // Allows access from any frontend (React/Angular/Postman)
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // 1. GET /api/categories - Get All (Flat list)
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 2. GET /api/categories/tree - Get Hierarchical Tree
    // Only fetches roots; children are loaded automatically via JPA relationships
    @GetMapping("/tree")
    public List<Category> getCategoryTree() {
        return categoryRepository.findByParentIsNull();
    }

    // 3. GET /api/categories/{id} - Get One
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryByQueryParam(@PathVariable("id") String id) {
        //Checks for valid UUID format to prevent Injection attacks
        if (!id.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")) {
            return ResponseEntity.badRequest().build();
        }
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. POST /api/categories - Create New
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    // 5. PUT /api/categories/{id} - Update
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") String id, @RequestBody Category details) {
        //Checks for valid UUID format to prevent Injection attacks
        if (!id.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")) {
            return ResponseEntity.badRequest().build();
        }
        return categoryRepository.findById(id).map(existing -> {
            // Update the Required fields if provided in the request body
            if (details.getName() != null) {
                existing.setName(details.getName());
            }
            if (details.getDescription() != null) {
                existing.setDescription(details.getDescription());
            }
            if (details.getIcon() != null) {
                existing.setIcon(details.getIcon());
            }
            if (details.getColor() != null) {
                existing.setColor(details.getColor());
            }
            if (details.getIsActive() != null) {
                existing.setIsActive(details.getIsActive());
            }
            // Update parent if provided
            if (details.getParent() != null && details.getParent().getId() != null) {
                existing.setParent(details.getParent());
            }
            existing.setUpdatedAt(java.time.LocalDateTime.now()); // Prevent updating createdAt
            return ResponseEntity.ok(categoryRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 6. DELETE /api/categories/{id} - Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") String id) {
        //Checks for valid UUID format to prevent Injection attacks
        if (!id.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$")) {
            return ResponseEntity.badRequest().build();
        }
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}