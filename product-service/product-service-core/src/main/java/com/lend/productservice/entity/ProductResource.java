package com.lend.productservice.entity;

import com.lend.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("product_resource")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResource {

    @Id
    private String id;

    @Field("image_url")
    private String imageUrl;

    @Field("is_active")
    private Status isActive = Status.ACTIVE;

    @Field("product_id")
    private String productId;

}
