package com.lender.productservice.service;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ResponseEntity<BaseResponse<ProductResponse>> create(ProductRequest request);

    ResponseEntity<BaseResponse<String>> uploadImage(String id, MultipartFile file) throws IOException;

    void storeImagePath(String productId, String path);

    ResponseEntity<BaseResponse<ProductResponse>> getById(String id);
}
