package com.lender.productservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productserviceshare.enumerate.ProductState;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@SQLDelete(sql = "UPDATE product_tbl SET is_active = 'INACTIVE' WHERE id=?")
@FilterDef(name = "deleteProductFilter",
        parameters = @ParamDef(name = "status", type = String.class))
@Filter(name = "deleteProductFilter", condition = "is_active = :status")
public class Product {

    @Id
    @GenericGenerator(name = "custom_product_id", strategy = "com.lender.productservice.util.CustomProductIdGenerator")
    @GeneratedValue(generator = "custom_product_id")
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "brand")
    private String brand;

    @Column(name = "is_active")
    @Enumerated(EnumType.STRING)
    private Status isActive = Status.ACTIVE;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private Set<ProductResource> productResources = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToOne(mappedBy = "product")
    private Commodity commodity;

}
