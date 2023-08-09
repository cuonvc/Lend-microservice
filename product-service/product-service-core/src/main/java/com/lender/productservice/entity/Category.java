package com.lender.productservice.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lender.baseservice.constant.enumerate.Status;
import jakarta.persistence.*;
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
@FilterDef(name = "deletedCategoryFilter", parameters = @ParamDef(name = "status", type = String.class))
@Filter(name = "deletedCategoryFilter", condition = "is_active = :status")
public class Category {

    @Id
    @GenericGenerator(name = "custom_category_id", strategy = "com.lender.productservice.util.CustomCategoryIdGenerator")
    @GeneratedValue(generator = "custom_category_id")
    private String id;

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
