package com.lender.productservice.service;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.ProductResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    ResponseEntity<BaseResponse<ProductResponse>> create(ProductRequest request);

    ResponseEntity<BaseResponse<ProductResponse>> getById(String id);
}
