package com.inventory.backend_api.repository;

import com.inventory.backend_api.entity.VariantOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VariantOptionRepository extends JpaRepository<VariantOption, String> {

    // Find all options of a specific type (e.g., all DROPDOWNs)
    List<VariantOption> findByType(VariantOption.OptionType type);
}