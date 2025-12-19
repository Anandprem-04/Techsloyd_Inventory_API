package com.inventory.backend_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "variant_options")
@Data
public class VariantOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    private Integer position = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OptionType type;

    // Defines the Enum inside the class or separately
    public enum OptionType {
        BUTTON, DROPDOWN, SWATCH
    }
}