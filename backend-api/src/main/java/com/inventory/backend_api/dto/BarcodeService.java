package com.inventory.backend_api.dto;
import com.inventory.backend_api.entity.*;
import com.inventory.backend_api.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BarcodeService {

    @Autowired private BarcodeRepository barcodeRepo;
    @Autowired private ProductRepository productRepo;
    @Autowired private ProductVariantRepository variantRepo;

    // 1. LOOKUP / SCAN
    public ScanResponse scanBarcode(String code) {
        Optional<Barcode> record = barcodeRepo.findByBarcode(code);

        if (record.isEmpty()) {
            return ScanResponse.builder().found(false).barcode(code).build();
        }

        Barcode b = record.get();

        // Scenario A: Barcode points to a Variant (Specific Item)
        if (b.getProductVariant() != null) {
            ProductVariant v = b.getProductVariant();
            Product p = v.getProduct();

            return ScanResponse.builder()
                    .found(true)
                    .barcode(code)
                    .type("VARIANT")
                    .name(p.getName() + " (" + v.getSku() + ")") // e.g., "T-Shirt (RED-S)"
                    .description(p.getDescription())
                    .sku(v.getSku())
                    .price(v.getPrice())
                    .stockLevel(v.getStockLevel())
                    .productId(p.getId())
                    .variantId(v.getId())
                    .build();
        }

        // Scenario B: Barcode points to a Generic Product
        else if (b.getProduct() != null) {
            Product p = b.getProduct();

            return ScanResponse.builder()
                    .found(true)
                    .barcode(code)
                    .type("PRODUCT")
                    .name(p.getName())
                    .description(p.getDescription())
                    .sku(p.getSku())
                    .price(p.getPrice())
                    .stockLevel(p.getStockLevel())
                    .productId(p.getId())
                    .variantId(null)
                    .build();
        }

        return ScanResponse.builder().found(false).build();
    }

    // 2. ASSIGN BARCODE (Link code to Product/Variant)
    public Barcode assignBarcode(String code, String format, String targetId, String type) {
        if (!validateChecksum(code, format)) {
            throw new IllegalArgumentException("Invalid Checksum for format " + format);
        }
        if (barcodeRepo.existsByBarcode(code)) {
            throw new IllegalArgumentException("Barcode already registered");
        }

        Barcode barcode = Barcode.builder()
                .barcode(code)
                .format(format)
                .build();

        if ("VARIANT".equalsIgnoreCase(type)) {
            ProductVariant v = variantRepo.findById(targetId)
                    .orElseThrow(() -> new RuntimeException("Variant not found"));
            barcode.setProductVariant(v);
        } else {
            Product p = productRepo.findById(targetId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            barcode.setProduct(p);
        }

        return barcodeRepo.save(barcode);
    }

    // 3. REAL-WORLD VALIDATION ALGORITHM
    // Validates EAN-13 and UPC-A check digits (Standard Global Trade Item Number logic)
    public boolean validateChecksum(String code, String format) {
        if ("CODE_128".equals(format)) return true; // Alphanumeric, logic varies
        if (code == null || !code.matches("\\d+")) return false; // Must be digits

        int length = code.length();
        if (!("EAN_13".equals(format) && length == 13) &&
                !("UPC_A".equals(format) && length == 12)) {
            return false; // Wrong length
        }

        // Luhn-like algorithm for GTIN
        int sum = 0;
        boolean multiplyBy3 = false; // Start from right, multiplier alternates 1 and 3

        // Loop from right to left, excluding the last digit (check digit)
        for (int i = length - 2; i >= 0; i--) {
            int digit = Character.getNumericValue(code.charAt(i));
            multiplyBy3 = !multiplyBy3; // Toggle multiplier
            // EAN-13 logic: Even pos * 3, Odd * 1 (from right)
            // But since we excluded check digit, we alternate 3, 1, 3, 1...
            sum += (multiplyBy3 ? 3 : 1) * digit;
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        int actualCheckDigit = Character.getNumericValue(code.charAt(length - 1));

        return checkDigit == actualCheckDigit;
    }
}