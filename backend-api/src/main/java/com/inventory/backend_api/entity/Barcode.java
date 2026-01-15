package com.inventory.backend_api.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "barcodes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Barcode {

    @Id
    @Column(nullable = false, unique = true)
    private String barcode; // The actual code (e.g. "012345678905")

    @Column(nullable = false)
    private String format; // EAN_13, UPC_A, CODE_128

    // Link to Generic Product (e.g. Water Bottle)
    @OneToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;

    // Link to Specific Variant (e.g. Red T-Shirt)
    @OneToOne
    @JoinColumn(name = "product_variant_id")
    @JsonIgnore
    private ProductVariant productVariant;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp // Optimization: Replaces manual @PrePersist
    private LocalDateTime createdAt;

}