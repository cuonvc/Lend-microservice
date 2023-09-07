package com.lender.productservice.service;

import com.lender.productservice.entity.Product;
import com.lender.productservice.entity.ProductResource;
import com.lender.productserviceshare.payload.response.ProductResourceResponse;

import java.util.List;

public interface ProductResourceService {

    List<ProductResourceResponse> getImageUrls(String productId);

    List<ProductResource> initResources(Product product);

    List<ProductResource> getByProduct(Product product);

    void storeImagePath(String resourceId, String path);

//    void clearImagePath(String productId);
}
