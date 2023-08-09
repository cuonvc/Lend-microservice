package com.lender.productserviceshare.payload.request;

import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.payload.CategoryDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductRequest {

    @NotNull
    @NotBlank(message = "Product name can't blank")
    @NotEmpty
    private String name;

    private String description;
    private String brand;
    private ProductState state;

    @NotNull
    private Double standardPrice;

    @NotNull
    @Size(min = 1, max = 5, message = "Categories must be at least 1 item")
    private Set<String> categoryIds;
}
