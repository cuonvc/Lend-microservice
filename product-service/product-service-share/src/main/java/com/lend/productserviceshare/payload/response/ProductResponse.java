package com.lend.productserviceshare.payload.response;

import com.lend.productserviceshare.payload.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {

    private String id;
    private String name;
    private String description;
    private List<ProductResourceResponse> resources;
    private String brand;
    private Set<CategoryDto> categories;
}
