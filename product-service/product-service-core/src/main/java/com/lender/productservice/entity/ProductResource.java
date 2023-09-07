package com.lender.productservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "product_resource_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResource {

    @Id
    @GenericGenerator(name = "custom_resource_id", strategy = "com.lender.productservice.util.CustomResourceIdGenerator")
    @GeneratedValue(generator = "custom_resource_id")
    private String id;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
