package com.lender.productservice.service.impl;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.baseservice.payload.request.FileObjectRequest;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productservice.configuration.CustomUserDetail;
import com.lender.productservice.entity.Category;
import com.lender.productservice.entity.Product;
import com.lender.productservice.exception.APIException;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.mapper.ProductMapper;
import com.lender.productservice.repository.CategoryRepository;
import com.lender.productservice.repository.ProductRepository;
import com.lender.productservice.service.ProductService;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String THUMB = "imageUrl";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ResponseFactory responseFactory;
    private final StreamBridge streamBridge;

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> create(ProductRequest request) {

        Set<Category> categories = request.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findByIdAndStatus(categoryId, Status.ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId)))
                .collect(Collectors.toSet());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();

        Product product = productMapper.requestToEntity(request);
        product.setUserId(userDetail.getId());
        product.setCode(generateProductCode(request.getState()));
        product.setCategories(categories);
        return responseFactory.success("Success", productMapper.entityToResponse(productRepository.save(product)));
    }

    @Override
    public ResponseEntity<BaseResponse<String>> uploadImage(String id, MultipartFile file) throws IOException {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Product product = productRepository.findByIdAndOwner(id, Status.ACTIVE, customUserDetail.getId())
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Access denied"));

        Message<FileObjectRequest> message = MessageBuilder
                .withPayload(FileObjectRequest.builder()
                        .field(THUMB)
                        .fileBytes(file.getBytes())
                        .build())
                .setHeader(KafkaHeaders.KEY, product.getId().getBytes())
                .build();

        streamBridge.send("product-image-request", message);
        return responseFactory.success("Pending", "Saving image...");
    }

    @Override
    public void storeImagePath(String productId, String path) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId)); //not happen

        product.setImageUrl(path);
        product.setModifiedDate(LocalDateTime.now());
        productRepository.save(product);
    }

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> getById(String id) {
        Product product = productRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        return responseFactory.success("Success", productMapper.entityToResponse(product));
    }

    private String generateProductCode(ProductState productState) {
        Random random = new Random();
        return productState.name().charAt(0) + String.valueOf(random.nextInt(10000));
    }
}
