package com.lend.productservice.service;

import com.lend.productservice.entity.Commodity;
import com.lend.productservice.entity.Product;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.productservice.entity.ProductResource;
import com.lend.productserviceshare.payload.request.CommodityRequest;
import com.lend.productserviceshare.payload.request.ProductRequest;
import com.lend.productserviceshare.payload.response.PageResponseProduct;
import com.lend.productserviceshare.payload.response.ProductResponse;
import org.springframework.http.ResponseEntity;

public interface ProductService {
    Product create(Commodity commodity, CommodityRequest request, String userId);

    Product update(Product product, ProductRequest request);

    ResponseEntity<BaseResponse<ProductResponse>> getById(String id);

    ResponseEntity<BaseResponse<PageResponseProduct>> findAllByActive(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ResponseEntity<BaseResponse<PageResponseProduct>> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ResponseEntity<BaseResponse<PageResponseProduct>> findAllByOwner(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
//    ResponseEntity<BaseResponse<String>> delete(String id);
//
//    ResponseEntity<BaseResponse<ProductResponse>> restore(String id);
}
