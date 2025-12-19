package com.inventory.backend_api.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "variant_combinations")
@Data
public class VariantCombination {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "variant_option_value_id", nullable = false)
    private VariantOptionValue variantOptionValue;
}