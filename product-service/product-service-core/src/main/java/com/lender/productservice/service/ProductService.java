package com.lender.productservice.service;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productservice.entity.Product;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.PageResponseProduct;
import com.lender.productserviceshare.payload.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    Product create(ProductRequest request);

    Product update(Product product, ProductRequest request);

    ResponseEntity<BaseResponse<ProductResponse>> getById(String id);

//    ResponseEntity<BaseResponse<PageResponseProduct>> findAllByFilter(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
//
//    ResponseEntity<BaseResponse<PageResponseProduct>> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
//
//    ResponseEntity<BaseResponse<String>> delete(String id);
//
//    ResponseEntity<BaseResponse<ProductResponse>> restore(String id);
}
