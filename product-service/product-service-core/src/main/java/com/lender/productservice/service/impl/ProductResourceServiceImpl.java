package com.lender.productservice.service.impl;

import com.lender.baseservice.constant.ConstantVariable;
import com.lender.productservice.entity.Product;
import com.lender.productservice.entity.ProductResource;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.mapper.ProductMapper;
import com.lender.productservice.repository.ProductResourceRepository;
import com.lender.productservice.service.ProductResourceService;
import com.lender.productserviceshare.payload.response.ProductResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductResourceServiceImpl implements ProductResourceService {

    private final ProductResourceRepository resourceRepository;

    @Override
    public List<ProductResource> initResources(Product product) {
        List<ProductResource> resources = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            ProductResource resource = new ProductResource();
            resource.setProduct(product);
            resources.add(resourceRepository.save(resource));
        }

        return resources;
    }

    @Override
    public List<ProductResource> getByProduct(Product product) {
        return resourceRepository.findByProductId(product.getId());
    }

    @Override
    public void storeImagePath(String resourceId, String path) {
        ProductResource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Ảnh sản phẩm", "id", resourceId)); //not happen

        resource.setImageUrl(path);
        resourceRepository.save(resource);
    }

//    @Override
//    @Transactional
//    public void clearImagePath(String productId) {
//        resourceRepository.deleteByProductId(productId);
//    }

    @Override
    public List<ProductResourceResponse> getImageUrls(String productId) {
        return resourceRepository.findByProductId(productId)
                .stream().map(element -> {
                    String path = element.getImageUrl();
                    if (path != null && path.contains("http")) {
                        return ProductResourceResponse.builder()
                                .id(element.getId())
                                .imageUrl(path)
                                .build();
                    }

                    return ProductResourceResponse.builder()
                            .id(element.getId())
                            .imageUrl(ConstantVariable.BASE_RESOURCE_DOMAIN + path)
                            .build();
                }).toList();
    }
}
