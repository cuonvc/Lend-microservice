package com.lend.productservice.mapper;

import com.lend.baseservice.constant.ConstantVariable;
import com.lend.productservice.entity.Product;
import com.lend.productserviceshare.payload.request.ProductRequest;
import com.lend.productserviceshare.payload.response.ProductResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ProductMapper {

    Product requestToEntity(ProductRequest productRequest);

    void requestToEntity(ProductRequest productRequest, @MappingTarget Product product);

//    @Mapping(source = "imageUrl", target = "imageUrl", qualifiedByName = "pathToUrl")
    ProductResponse entityToResponse(Product product);

    @Named("pathToUrl")
    static String pathToUrl(String path) {
        if (path != null && path.contains("http")) {
            return path;
        }
        return ConstantVariable.BASE_RESOURCE_DOMAIN + path;
    }
}
