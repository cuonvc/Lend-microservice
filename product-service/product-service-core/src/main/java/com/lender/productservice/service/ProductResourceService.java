package com.lender.productservice.service;

import com.lender.productservice.entity.Product;
import com.lender.productservice.entity.ProductResource;

import java.util.List;

public interface ProductResourceService {

    List<String> getImageUrls(String productId);

    ProductResource initResource(Product product);

    void storeImagePath(String resourceId, String path);
}
