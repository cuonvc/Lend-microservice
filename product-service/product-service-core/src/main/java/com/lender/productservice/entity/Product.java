package com.lender.productservice.entity;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productserviceshare.enumerate.ProductState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

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

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "modified_by", nullable = false)
    private String modifiedBy;

    @ManyToMany
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

}
