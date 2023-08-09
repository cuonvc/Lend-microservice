package com.lender.productserviceshare.payload.response;

import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.payload.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductResponse {

    private String id;
    private String name;
    private String code;
    private String description;
    private String imageUrl;
    private String brand;
    private ProductState state;
    private Double standardPrice;
    private Double salePrice;
    private LocalDateTime saleExpireAt;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Set<CategoryDto> categories;
    private String userId;
}
