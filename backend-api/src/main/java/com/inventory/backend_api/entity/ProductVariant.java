package com.inventory.backend_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product_variants")
@Data
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false)
    private BigDecimal price;

    private BigDecimal cost;

    @Column(name = "stock_level")
    private Integer stockLevel = 0;

    @Column(name = "is_active")
    private Boolean isActive = true;

    // This connects to the 6th table (Combinations)
    @OneToMany(mappedBy = "productVariant", cascade = CascadeType.ALL)
    private List<VariantCombination> combinations;
}