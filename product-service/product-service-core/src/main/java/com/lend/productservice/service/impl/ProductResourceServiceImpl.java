package com.lend.productservice.service.impl;

import com.lend.productservice.service.ProductResourceService;
import com.lend.baseservice.constant.ConstantVariable;
import com.lend.productservice.entity.Product;
import com.lend.productservice.entity.ProductResource;
import com.lend.productservice.exception.ResourceNotFoundException;
import com.lend.productservice.repository.ProductResourceRepository;
import com.lend.productserviceshare.payload.response.ProductResourceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
