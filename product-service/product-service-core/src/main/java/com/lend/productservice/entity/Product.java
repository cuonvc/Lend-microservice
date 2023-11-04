package com.lend.productservice.entity;

import com.lend.authserviceshare.payload.response.UserResponse;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.productserviceshare.enumerate.ProductState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document("product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Product {

    @Id
    private String id;

    private String name;

    private String description;

    private String brand;

    @Field("standard_price")
    private Double standardPrice;

    @Field("sale_price")
    private Double salePrice;

    @Field("sale_expire_at")
    private LocalDateTime saleExpireAt;

    private Integer remaining = 0;

    @Field("is_active")
    private Status isActive = Status.ACTIVE;

    @Field("commodity_id")
    private String commodityId;

    @Field("user_id")
    private String userId;

    @Field("resources")
    private Set<ProductResource> resources = new HashSet<>();

    @Field("categories")
    private Set<Category> categories = new HashSet<>();

}
