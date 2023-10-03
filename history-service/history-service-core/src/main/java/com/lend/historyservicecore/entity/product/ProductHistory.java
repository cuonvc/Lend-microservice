package com.lend.historyservicecore.entity.product;

import com.lend.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashSet;
import java.util.Set;

@Document("history_product")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class ProductHistory {

    @Id
    private String id;

    @Field("product_id")
    private String productId;

    @Field("name")
    private String name;

    @Field("description")
    private String description;

    @Field("brand")
    private String brand;

    @Field("is_active")
    private Status isActive = Status.ACTIVE;

    @Field("resources")
    private Set<ProductResourceHistory> resources = new HashSet<>();

    @Field("categories")
    private Set<CategoryHistory> categories = new HashSet<>();
}
