package com.inventory.backend_api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.inventory.backend_api.dto.*;
import com.inventory.backend_api.entity.Category;
import com.inventory.backend_api.repository.CategoryRepository;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin("*") 
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    // UUID Regex Pattern
    // Used to distinguish between special keywords (like "statistics") and UUIDs
    private final String UUID_PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

    // GET /api/categories/statistics
    @GetMapping("/statistics")
    public ResponseEntity<CategoryStatsResponse> getCategoryStatistics() {
        long total = categoryRepository.count();
        long active = categoryRepository.countByIsActiveTrue();
        long inactive = categoryRepository.countByIsActiveFalse();
        long roots = categoryRepository.countByParentIsNull();

        return ResponseEntity.ok(CategoryStatsResponse.builder()
                .totalCategories(total)
                .activeCategories(active)
                .inactiveCategories(inactive)
                .rootCategories(roots)
                .build());
    }

    // GET /api/categories/tree
    @GetMapping("/tree")
    public List<Category> getCategoryTree() {
        return categoryRepository.findByParentIsNull();
    }

    // GET /api/categories (Flat list)
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // POST /api/categories/move
    @PostMapping("/move")
    public ResponseEntity<Category> moveCategory(@RequestBody MoveCategoryRequest req) {
        String categoryId = req.getCategoryId();
        String newParentId = req.getNewParentId();

        // 1. Validate Category ID
        if (categoryId == null || !categoryId.matches(UUID_PATTERN)) {
            return ResponseEntity.badRequest().build();
        }

        // 2. Find the Category to move
        var maybeCategory = categoryRepository.findById(categoryId);
        if (maybeCategory.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Category category = maybeCategory.get();

        Category newParent = null;

        // 3. Handle Parent Logic
        if (newParentId != null && !newParentId.isBlank()) {
            // Validate Parent ID
            if (!newParentId.matches(UUID_PATTERN)) {
                return ResponseEntity.badRequest().build();
            }
            // Self-parenting check
            if (newParentId.equals(categoryId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
            var maybeParent = categoryRepository.findById(newParentId);
            if (maybeParent.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            newParent = maybeParent.get();

            // 4. Cycle Detection (Crucial for Trees)
            // Walk up the tree from the new parent to ensure we don't hit the category we are moving
            Category cursor = newParent;
            while (cursor != null) {
                if (cursor.getId().equals(categoryId)) {
                    // Cycle detected!
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                }
                cursor = cursor.getParent();
            }
        }

        // 5. Perform the move
        category.setParent(newParent);
        category.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    // POST /api/categories/reorder
    @PostMapping("/reorder")
    public ResponseEntity<?> reorderCategories(@RequestBody CategoryReorderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body("No items to reorder");
        }

        // 1. Extract IDs from request
        List<String> ids = request.getItems().stream()
                .map(CategoryReorderRequest.ReorderItem::getId)
                .collect(Collectors.toList());

        // 2. Fetch all affected categories in one query
        List<Category> categories = categoryRepository.findAllById(ids);

        if (categories.size() != ids.size()) {
            return ResponseEntity.badRequest().body("One or more Category IDs are invalid");
        }

        // 3. Create a map for fast lookup: ID -> New Position
        Map<String, Integer> positionMap = request.getItems().stream()
                .collect(Collectors.toMap(
                        CategoryReorderRequest.ReorderItem::getId,
                        CategoryReorderRequest.ReorderItem::getPosition
                ));

        // 4. Update positions in memory
        categories.forEach(cat -> {
            if (positionMap.containsKey(cat.getId())) {
                cat.setPosition(positionMap.get(cat.getId()));
                cat.setUpdatedAt(LocalDateTime.now());
            }
        });

        // 5. Bulk save
        categoryRepository.saveAll(categories);
        return ResponseEntity.ok().build();
    }

    // POST /api/categories (Create)
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    // GET /api/categories/{id}
    @GetMapping("/{id:" + UUID_PATTERN + "}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") String id) {
        return categoryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/categories/{id}
    @PutMapping("/{id:" + UUID_PATTERN + "}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") String id, @RequestBody Category details) {
        return categoryRepository.findById(id).map(existing -> {
            if (details.getName() != null) existing.setName(details.getName());
            if (details.getDescription() != null) existing.setDescription(details.getDescription());
            if (details.getIcon() != null) existing.setIcon(details.getIcon());
            if (details.getColor() != null) existing.setColor(details.getColor());
            if (details.getIsActive() != null) existing.setIsActive(details.getIsActive());
            
            // Handle parent update (optional)
            if (details.getParent() != null && details.getParent().getId() != null) {
                 existing.setParent(details.getParent());
            } else if (details.getParent() == null) {
                // Logic to clear parent if explicit null provided? 
                // Usually handled via separate "move" endpoint, but safe to ignore here or handle if needed.
            }

            existing.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(categoryRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/categories/{id}
    @DeleteMapping("/{id:" + UUID_PATTERN + "}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}