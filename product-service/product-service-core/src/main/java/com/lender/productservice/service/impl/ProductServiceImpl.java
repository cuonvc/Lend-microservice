package com.lender.productservice.service.impl;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productservice.configuration.CustomUserDetail;
import com.lender.productservice.entity.Category;
import com.lender.productservice.entity.Product;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.mapper.ProductMapper;
import com.lender.productservice.repository.CategoryRepository;
import com.lender.productservice.repository.ProductRepository;
import com.lender.productservice.service.ProductService;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> create(ProductRequest request) {

        Set<Category> categories = request.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findByIdAndStatus(categoryId, Status.ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId)))
                .collect(Collectors.toSet());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        Product product = productMapper.requestToEntity(request);
        product.setUserId(userDetail.getId());
        product.setCode(generateProductCode(request.getState()));
        product.setCategories(categories);
        return responseFactory.success("Success", productMapper.entityToResponse(productRepository.save(product)));
    }

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> getById(String id) {
        Product product = productRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return responseFactory.success("Success", productMapper.entityToResponse(product));
    }

    private String generateProductCode(ProductState productState) {
        Random random = new Random();
        return productState.name().charAt(0) + String.valueOf(random.nextInt(10000));
    }
}
