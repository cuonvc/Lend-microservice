package com.lend.productserviceshare.payload.response;

import com.lend.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryResponse {

    private String id;

    private Set<CategoryResponse> children;

    private String name;

    private String description;

    private String imageUrl;

    private Status isActive;

    public void addChildren(CategoryResponse child) {
        if (children == null) {
            children = new HashSet<>();
        }
        children.add(child);
    }
}
