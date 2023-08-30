package com.lender.productservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productserviceshare.enumerate.ProductState;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_tbl", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code"})
})
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

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "brand")
    private String brand;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ProductState state = ProductState.OLD;

    @Column(name = "standard_price", nullable = false)
    private Double standardPrice;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "sale_expire_at")
    private LocalDateTime saleExpireAt;

    @Column(name = "is_active")
    @Enumerated(EnumType.STRING)
    private Status isActive = Status.ACTIVE;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Column(name = "user_id")
    private String userId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

}
