package com.inventory.backend_api.repository;

import com.inventory.backend_api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    // Search by Name OR SKU (Case Insensitive)
    List<Product> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);

    // products whose category id equals the given id or whose category's parent id equals the given id
    @Query("select p from Product p join p.category c where c.id = :categoryId or c.parent.id = :categoryId")
    Page<Product> findByCategoryOrParentId(@Param("categoryId") String categoryId, Pageable pageable);
    // Find all products in a specific category
    List<Product> findByCategoryId(String categoryId);
}