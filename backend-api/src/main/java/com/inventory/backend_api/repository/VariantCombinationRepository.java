package com.inventory.backend_api.repository;

import com.inventory.backend_api.entity.VariantCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariantCombinationRepository extends JpaRepository<VariantCombination, String> {
    // Standard CRUD is sufficient here for now
}