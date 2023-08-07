package com.lender.productservice.service.impl;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productservice.entity.Product;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.mapper.ProductMapper;
import com.lender.productservice.repository.CategoryRepository;
import com.lender.productservice.repository.ProductRepository;
import com.lender.productservice.service.ProductService;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> create(ProductRequest request) {

        request.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId)));

        Product product = productRepository.save(productMapper.requestToEntity(request));
        return responseFactory.success("Success", productMapper.entityToResponse(product));
    }
}
