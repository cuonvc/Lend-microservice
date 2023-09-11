package com.lend.productservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lend.baseservice.constant.enumerate.Status;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "category_tbl", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder(toBuilder = true)
@SQLDelete(sql = "UPDATE category_tbl SET is_active = 'INACTIVE' WHERE id=?")
@FilterDefs({
        @FilterDef(name = "deletedCategoryFilter", parameters = {
                @ParamDef(name = "status", type = String.class),
        }),
        @FilterDef(name = "rootCategoryFilter", parameters = {
                @ParamDef(name = "parentId", type = String.class)
        })
})

@Filters({
        @Filter(name = "deletedCategoryFilter", condition = "is_active = :status"),
        @Filter(name = "rootCategoryFilter", condition = "parent_id = :parentId")
})
public class Category {

    @Id
    @GenericGenerator(name = "custom_category_id", strategy = "com.lend.productservice.util.CustomCategoryIdGenerator")
    @GeneratedValue(generator = "custom_category_id")
    private String id;

    @Column(name = "parent_id")
    private String parentId = null;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_active")
    @Enumerated(EnumType.STRING)
    private Status isActive = Status.ACTIVE;

//    @ManyToMany(mappedBy = "categories")
//    @JsonManagedReference
//    private Set<Product> products = new HashSet<>();
}
