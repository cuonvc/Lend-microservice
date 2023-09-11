package com.lend.productserviceshare.payload;

import com.lend.baseservice.constant.enumerate.Status;
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
public class CategoryDto {

    private String id;

    private String parentId;

    @NotNull
    @NotBlank(message = "Name can't blank")
    @NotEmpty
    private String name;

    private String description;

    private Status isActive;
}
