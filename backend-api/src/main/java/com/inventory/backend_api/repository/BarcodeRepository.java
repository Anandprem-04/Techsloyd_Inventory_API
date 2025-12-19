package com.inventory.backend_api.repository;

import com.inventory.backend_api.entity.Barcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarcodeRepository extends JpaRepository<Barcode, String> {

    // Find barcode entry by the code itself (though findById does this too)
    Optional<Barcode> findByBarcode(String barcode);

    // Check if a barcode already exists
    boolean existsByBarcode(String barcode);
}