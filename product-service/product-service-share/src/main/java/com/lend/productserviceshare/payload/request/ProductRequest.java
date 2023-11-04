package com.lend.productserviceshare.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductRequest {

    @NotNull
    @NotBlank(message = "Tên sản phẩm không được để trống!")
    @NotEmpty
    private String name;

    private String description;
    private String brand;

    @NotNull
    private Double standardPrice;

    @NotNull
    private LocalDateTime expireDate;

    @Size(min = 1, max = 5, message = "Categories phải có ít nhất 1 item")
    private List<String> categoryIds;

    @Size(min = 1, max = 5, message = "Ảnh mô tả phải có ít nhất 1 item")
    private List<ProductResourceRequest> resources;
}
