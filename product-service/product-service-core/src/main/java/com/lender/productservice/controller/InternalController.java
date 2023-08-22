package com.lender.productservice.controller;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productservice.entity.Product;
import com.lender.productservice.mapper.ProductMapper;
import com.lender.productservice.repository.ProductRepository;
import com.lender.productserviceshare.payload.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class InternalController {

    private final ProductRepository productRepository;
    private final ResponseFactory responseFactory;
    private final ProductMapper productMapper;

    @GetMapping("/product/{productId}")
    public ResponseEntity<BaseResponse<ProductResponse>> getDetail(@PathVariable("productId") String id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return responseFactory.success("Not found", null);
        }

        return responseFactory.success("Success", productMapper.entityToResponse(product.get()));
    }
}
