package com.lend.productservice.mapper;

import com.lend.baseservice.constant.enumerate.Status;
import com.lend.productservice.entity.Category;
import com.lend.productserviceshare.payload.CategoryDto;
import com.lend.productserviceshare.payload.response.CategoryResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface CategoryMapper {

    @Mapping(target = "isActive", ignore = true)
    Category dtoToEntity(CategoryDto categoryDto);

    @Mapping(target = "parentId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void dtoToEntity(CategoryDto categoryDto, @MappingTarget Category category);

    CategoryDto entityToDto(Category category);

    CategoryResponse entityToResponse(Category category);

}
