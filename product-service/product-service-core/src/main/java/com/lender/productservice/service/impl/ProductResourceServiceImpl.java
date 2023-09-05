package com.lender.productservice.service.impl;

import com.lender.baseservice.constant.ConstantVariable;
import com.lender.productservice.entity.Product;
import com.lender.productservice.entity.ProductResource;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.mapper.ProductMapper;
import com.lender.productservice.repository.ProductResourceRepository;
import com.lender.productservice.service.ProductResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductResourceServiceImpl implements ProductResourceService {

    private final ProductResourceRepository resourceRepository;

    @Override
    public ProductResource initResource(Product product) {
        ProductResource resource = new ProductResource();
        resource.setProduct(product);
        return resourceRepository.save(resource);
    }

    @Override
    public void storeImagePath(String resourceId, String path) {
        ProductResource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ảnh sản phẩm", "id", resourceId)); //not happen

        resource.setImageUrl(path);
        resourceRepository.save(resource);
    }

    @Override
    public List<String> getImageUrls(String productId) {
        return resourceRepository.findByProductId(productId)
                .stream().map(element -> {
                    String path = element.getImageUrl();
                    if (path != null && path.contains("http")) {
                        return path;
                    }
                    return ConstantVariable.BASE_RESOURCE_DOMAIN + path;
                }).toList();
    }
}
