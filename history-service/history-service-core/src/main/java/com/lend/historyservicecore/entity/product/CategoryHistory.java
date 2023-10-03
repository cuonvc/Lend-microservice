package com.lend.historyservicecore.entity.product;

import com.lend.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryHistory {

    @Id
    private String id;

    @Field("category_id")
    private String categoryId;

    @Field("parent_id")
    private String parentId = null;

    private String name;

    private String description;

    @Field("is_active")
    private Status isActive = Status.ACTIVE;

}
