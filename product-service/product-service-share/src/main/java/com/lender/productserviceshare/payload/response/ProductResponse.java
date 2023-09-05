package com.lender.productserviceshare.payload.response;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.payload.CategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<String> imageUrls;
    private String brand;
    private Set<CategoryDto> categories;
}
