package com.lender.productserviceshare.payload.response;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productserviceshare.payload.CategoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryResponse {

    private String id;

    private CategoryResponse parent;

    private String name;

    private String description;

    private Status isActive;
}
