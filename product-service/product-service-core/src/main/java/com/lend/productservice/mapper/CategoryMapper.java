package com.lend.productservice.mapper;

import com.lend.baseservice.constant.ConstantVariable;
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
    @Mapping(target = "imageUrl", ignore = true)
    Category dtoToEntity(CategoryDto categoryDto);

    @Mapping(target = "parentId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void dtoToEntity(CategoryDto categoryDto, @MappingTarget Category category);

    CategoryDto entityToDto(Category category);

    @Mapping(source = "imageUrl", target = "imageUrl", qualifiedByName = "pathToUrl")
    CategoryResponse entityToResponse(Category category);

    @Named("pathToUrl")
    static String pathToUrl(String path) {
        if (path != null && path.contains("http")) {
            return path;
        }
        return ConstantVariable.BASE_RESOURCE_DOMAIN + path;
    }

}
