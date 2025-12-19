package com.inventory.backend_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "barcodes")
@Data
public class Barcode {

    @Id
    @Column(nullable = false, unique = true)
    private String barcode; // The barcode string itself is the ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BarcodeFormat format;

    // Can point to a Product...
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // ... OR a Variant (but not both)
    @OneToOne
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum BarcodeFormat {
        UPC_A, UPC_E, EAN_13, EAN_8, CODE_128
    }
}