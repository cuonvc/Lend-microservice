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

import java.util.List;
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

    private Status isActive;
}
