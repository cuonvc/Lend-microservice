package com.lender.productservice.mapper;

import com.lender.baseservice.constant.ConstantVariable;
import com.lender.productservice.entity.Product;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.ProductResponse;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        builder = @Builder(disableBuilder = true))
public interface ProductMapper {

    Product requestToEntity(ProductRequest productRequest);

    @Mapping(source = "imageUrl", target = "imageUrl", qualifiedByName = "pathToUrl")
    ProductResponse entityToResponse(Product product);

    @Named("pathToUrl")
    static String pathToUrl(String path) {
        if (path != null && path.contains("http")) {
            return path;
        }
        return ConstantVariable.BASE_RESOURCE_DOMAIN + path;
    }
}
