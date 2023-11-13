package com.lend.productservice.service.impl;

import com.lend.productservice.entity.Category;
import com.lend.productservice.entity.Commodity;
import com.lend.productservice.repository.CategoryRepository;
import com.lend.productservice.repository.CommodityRepository;
import com.lend.productservice.repository.ProductRepository;
import com.lend.productservice.repository.custom.ResourceCustomRepository;
import com.lend.productservice.service.ProductResourceService;
import com.lend.baseservice.constant.ConstantVariable;
import com.lend.productservice.entity.Product;
import com.lend.productservice.entity.ProductResource;
import com.lend.productservice.exception.ResourceNotFoundException;
import com.lend.productservice.repository.ProductResourceRepository;
import com.lend.productservice.service.ProductService;
import com.lend.productserviceshare.payload.response.ProductResourceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductResourceServiceImpl implements ProductResourceService {

    private final ProductResourceRepository resourceRepository;
    private final ProductRepository productRepository;
    private final CommodityRepository commodityRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public List<ProductResource> initResources(Product product) {
        List<ProductResource> resources = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            ProductResource resource = new ProductResource();
            resource.setProductId(product.getId());
            resources.add(resourceRepository.save(resource));
        }

        return resources;
    }

    @Override
    public List<ProductResource> getByProduct(Product product) {
        return resourceRepository.findByProductId(product.getId());
    }

    @Override
    public void storeImagePath(String id, String field, String path) {
        switch (field) {
            case "PRODUCT" -> {
                ProductResource resource = resourceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Ảnh sản phẩm", "id", id)); //not happen

                resource.setImageUrl(path);
                resourceRepository.save(resource);
                updateResourceInProduct(resource);
            }

            case "CATEGORY" -> {
                Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

                category.setImageUrl(path);
                categoryRepository.save(category);
            }

            default -> System.out.println("Not trigger");
        }

    }

    private void updateResourceInProduct(ProductResource resource) {
        Product product = productRepository.findById(resource.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Tài nguyên Sản phẩm", "id", resource.toString())); //not happen

        product.getResources().stream()
                .filter(r -> r.getId().equals(resource.getId()))
                .peek(r -> {
                    log.info("Triggerr 70 - {}", r);
                    r.setImageUrl(resource.getImageUrl());
                }).collect(Collectors.toSet());
        productRepository.save(product);
        updateResourceInCommodity(product);
    }

    private void updateResourceInCommodity(Product product) {
        Commodity commodity = commodityRepository.findById(product.getCommodityId())
                .orElseThrow(() -> new ResourceNotFoundException("Mặt hàng", "sản phẩm", product.getCommodityId()));

        Set<ProductResource> resources = product.getResources();
        resources.forEach(resource -> {
            commodity.getProduct().getResources().stream()
                    .filter(r -> r.getId().equals(resource.getId()))
                    .peek(r -> r.setImageUrl(resource.getImageUrl()))
                    .collect(Collectors.toSet());
        });

        commodityRepository.save(commodity);
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
