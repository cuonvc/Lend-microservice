package com.lend.productservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lend.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document("category")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
public class Category {

    @Id
    private String id;

    @Field("parent_id")
    private String parentId = null;

    private String name;

    private String description;

    private String imageUrl;

    @Field("is_active")
    private Status isActive = Status.ACTIVE;

//    @ManyToMany(mappedBy = "categories")
//    @JsonManagedReference
//    private Set<Product> products = new HashSet<>();
}
