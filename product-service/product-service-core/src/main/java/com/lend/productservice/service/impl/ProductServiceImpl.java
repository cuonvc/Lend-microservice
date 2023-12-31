package com.lend.productservice.service.impl;

import com.lend.authserviceshare.payload.response.UserResponse;
import com.lend.productservice.configuration.CustomUserDetail;
import com.lend.productservice.entity.Category;
import com.lend.productservice.entity.Commodity;
import com.lend.productservice.entity.Product;
import com.lend.productservice.mapper.ProductMapper;
import com.lend.productservice.repository.CategoryRepository;
import com.lend.productservice.repository.ProductRepository;
import com.lend.productservice.service.CommodityService;
import com.lend.productservice.service.ProductResourceService;
import com.lend.productservice.service.ProductService;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.baseservice.payload.request.FileObjectRequest;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import com.lend.productservice.entity.ProductResource;
import com.lend.productservice.exception.APIException;
import com.lend.productservice.exception.ResourceNotFoundException;
import com.lend.productserviceshare.payload.request.CommodityRequest;
import com.lend.productserviceshare.payload.request.ProductRequest;
import com.lend.productserviceshare.payload.request.ProductResourceRequest;
import com.lend.productserviceshare.payload.response.PageResponseProduct;
import com.lend.productserviceshare.payload.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String THUMB = "imageUrl";

    private final ProductRepository productRepository;
//    private final CategoryCustomRepository categoryCustomRepository;
    private final CategoryRepository categoryRepository;
    private final ProductResourceService resourceService;
    private final ProductMapper productMapper;
    private final ResponseFactory responseFactory;
    private final StreamBridge streamBridge;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public Product create(Commodity commodity, CommodityRequest request, String userId) {

        Set<Category> categories = request.getProductRequest().getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findByIdAndIsActive(categoryId, Status.ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId)))
                .collect(Collectors.toSet());

        Product product = productMapper.requestToEntity(request.getProductRequest());
        product.setRemaining(request.getSerialNumbers().size());
        product.setCategories(categories);
        product.setCommodityId(commodity.getId());
        product.setUserId(userId);
        productRepository.save(product);

        List<ProductResource> resources = resourceService.initResources(product);
        handleMapResource(request.getProductRequest(), product, resources);

        product.setResources(new HashSet<>(resources));
        product.getResources().forEach(resource -> resource.setImageUrl(""));
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Product product, ProductRequest request) {
        Set<Category> categories = request.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findByIdAndIsActive(categoryId, Status.ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId)))
                .collect(Collectors.toSet());

//        resourceService.clearImagePath(product.getId());
        productMapper.requestToEntity(request, product);
        product.setCategories(categories);
        handleMapResource(request, product, resourceService.getByProduct(product));

        return productRepository.save(product);
    }

    private void handleMapResource(ProductRequest request, Product product, List<ProductResource> resources) {
        int sizeRequest = request.getResources().size();
        if (sizeRequest < 5) {
            IntStream.range(0, 5 - sizeRequest)
                    .forEach(item -> request.getResources().add(ProductResourceRequest.builder()
                            .id(null)
                            .imageValue("")
                            .build()));
        }

        IntStream.range(0, resources.size()).forEach(index -> {
            String imageValue = request.getResources().get(index).getImageValue();
            resources.get(index).setImageUrl(imageValue);
            uploadImage(product.getId(), resources.get(index).getId(), imageValue);
        });
    }

    private void uploadImage(String productId, String resourceId, String imageValue) {
        Message<FileObjectRequest> message = MessageBuilder
                .withPayload(FileObjectRequest.builder()
                        .field(THUMB)
                        .fileBytes(Base64.getDecoder().decode(imageValue))
                        .build())
                .setHeader(KafkaHeaders.KEY, (productId + "/" + resourceId).getBytes())
                .build();

        streamBridge.send("product-image-request", message);
    }

    @Override
    public ResponseEntity<BaseResponse<ProductResponse>> getById(String id) {
        Product product = productRepository.findByIdAndIsActive(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "id", id));
        ProductResponse response = productMapper.entityToResponse(product);
        response.setResources(resourceService.getImageUrls(response.getId()));
        response.setUser(getUserInfo(product.getUserId()));

        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponseProduct>> findAllByActive(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findByIsActive(pageable, Status.ACTIVE);

        return responseFactory.success("Success", paging(productPage));
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponseProduct>> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        return responseFactory.success("Success", paging(productPage));
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponseProduct>> findAllByOwner(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> productPage = productRepository.findByUserId(pageable, userDetail.getId(), Status.ACTIVE);

        return responseFactory.success("Success", paging(productPage));
    }

    private UserResponse getUserInfo(String userId) {
        return Optional
                .ofNullable(restTemplate.exchange(
                        "http://AUTH-SERVICE/api/auth/account/" + userId,  //http or https???
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<BaseResponse<UserResponse>>() {}
                ).getBody().getData())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    //    @Override
//    public ResponseEntity<BaseResponse<String>> delete(String id) {
//
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
//
//        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        if (userDetail.getGrantedAuthorities().get(0).equals("USER") && !userDetail.getId().equals(product.getUserId())) {
////            return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Không được phép truy cập", null);
////        }
//
//        productRepository.delete(product);
//        return responseFactory.success("Success", "Đã xóa thành công " + product.getName());
//    }
//
//    @Override
//    public ResponseEntity<BaseResponse<ProductResponse>> restore(String id) {
//        Product product = productRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "id", id));
//
////        product.setIsActive(Status.ACTIVE);
//        return responseFactory.success("Success", productMapper.entityToResponse(productRepository.save(product)));
//    }
//
    private PageResponseProduct paging(Page<Product> productPage) {
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(product -> {
                    ProductResponse response = productMapper.entityToResponse(product);
                    response.setResources(resourceService.getImageUrls(response.getId()));
                    response.setUser(getUserInfo(product.getUserId()));
                    return response;
                })
                .toList();

        return (PageResponseProduct) PageResponseProduct.builder()
                .pageNo(productPage.getNumber())
                .pageSize(productResponses.size())
                .content(productResponses)
                .totalPages(productPage.getTotalPages())
                .totalItems((int) productPage.getTotalElements())
                .last(productPage.isLast())
                .build();
    }
}
