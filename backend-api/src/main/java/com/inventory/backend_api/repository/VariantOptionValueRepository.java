package com.inventory.backend_api.repository;

import com.inventory.backend_api.entity.VariantOptionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantOptionValueRepository extends JpaRepository<VariantOptionValue, String> {

    // Find all values for a specific option (e.g., all values for "Color")
    List<VariantOptionValue> findByVariantOptionId(String variantOptionId);
}