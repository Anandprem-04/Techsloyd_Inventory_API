package com.inventory.backend_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String sku;

    private String description;
    private String image;
    private String unit;

    // Pricing
    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @Column(name = "tax_rate")
    private BigDecimal taxRate;

    // Inventory
    @Column(name = "stock_level")
    private Integer stockLevel = 0;

    @Column(name = "reorder_level")
    private Integer reorderLevel = 10;

    @Column(nullable = false)
    private String status = "active"; // 'active' or 'inactive'

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}