package com.inventory.backend_api.repository;

import com.inventory.backend_api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    // Fetch only the "Root" categories (where parent is null)
    List<Category> findByParentIsNull();

    // Fetch children of a specific category
    List<Category> findByParentId(String parentId);
}