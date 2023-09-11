package com.lend.productservice.controller;

import com.lend.productservice.entity.Product;
import com.lend.productservice.mapper.ProductMapper;
import com.lend.productservice.repository.ProductRepository;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import com.lend.productserviceshare.payload.response.ProductResponse;
import lombok.RequiredArgsConstructor;
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
