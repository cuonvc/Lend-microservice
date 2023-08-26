package com.lender.productservice.mapper;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productservice.entity.Category;
import com.lender.productservice.exception.APIException;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.repository.CategoryRepository;
import com.lender.productserviceshare.payload.CategoryDto;
import com.lender.productserviceshare.payload.response.CategoryResponse;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
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
