package com.inventory.backend_api.repository;

import com.inventory.backend_api.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, String> {

    // Find all variants for a specific product
    List<ProductVariant> findByProductId(String productId);

    // Find a variant by its unique SKU
    Optional<ProductVariant> findBySku(String sku);
}