package com.inventory.backend_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "variant_option_values")
@Data
public class VariantOptionValue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "variant_option_id", nullable = false)
    private VariantOption variantOption;

    @Column(nullable = false)
    private String value; // "Red"

    @Column(name = "display_value")
    private String displayValue; // "#FF0000"

    private Integer position = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "price_adjustment_type")
    private PriceAdjustmentType priceAdjustmentType = PriceAdjustmentType.FIXED;

    @Column(name = "price_adjustment_value")
    private BigDecimal priceAdjustmentValue = BigDecimal.ZERO;

    public enum PriceAdjustmentType {
        FIXED, PERCENTAGE
    }
}