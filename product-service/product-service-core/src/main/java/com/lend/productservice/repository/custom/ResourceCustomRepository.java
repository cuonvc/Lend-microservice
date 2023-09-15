package com.lend.productservice.repository.custom;

import com.lend.productservice.entity.ProductResource;

import java.util.List;

public interface ResourceCustomRepository {

    List<ProductResource> findByProductId(String id);
}
