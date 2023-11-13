package com.lend.productservice.service;

import com.lend.productservice.entity.Product;
import com.lend.productservice.entity.ProductResource;
import com.lend.productserviceshare.payload.response.ProductResourceResponse;

import java.util.List;

public interface ProductResourceService {

    List<ProductResourceResponse> getImageUrls(String productId);

    List<ProductResource> initResources(Product product);

    List<ProductResource> getByProduct(Product product);

    void storeImagePath(String id, String field, String path);

//    void clearImagePath(String productId);
}
