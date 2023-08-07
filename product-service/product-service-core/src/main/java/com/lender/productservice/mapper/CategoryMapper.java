package com.lender.productservice.mapper;

import com.lender.productservice.entity.Category;
import com.lender.productserviceshare.payload.CategoryDto;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface CategoryMapper {

    Category dtoToEntity(CategoryDto categoryDto);

    void dtoToEntity(CategoryDto categoryDto, @MappingTarget Category category);

    @Mapping(source = "isActive", target = "status")
    CategoryDto entityToDto(Category category);
}
