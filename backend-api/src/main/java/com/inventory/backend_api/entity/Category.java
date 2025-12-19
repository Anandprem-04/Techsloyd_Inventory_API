package com.inventory.backend_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private String description;
    private String icon;
    private String color;
    private String image;

    @Column(name = "is_active")
    private Boolean isActive = true;

    private Integer position = 0;

    @Column(name = "product_count")
    private Integer productCount = 0;

    // --- SELF-REFERENCING RELATIONSHIP (TREE) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @ToString.Exclude
    @JsonBackReference // Prevents infinite recursion (Child -> Parent -> Child...)
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonManagedReference
    private List<Category> children = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}