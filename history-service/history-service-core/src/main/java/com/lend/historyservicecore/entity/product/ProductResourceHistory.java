package com.lend.historyservicecore.entity.product;

import com.lend.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductResourceHistory {

    @Id
    private String id;

    @Field("image_url")
    private String imageUrl;

    @Field("is_active")
    private Status isActive = Status.ACTIVE;

    @Field("product_id")
    private String productId;

}
