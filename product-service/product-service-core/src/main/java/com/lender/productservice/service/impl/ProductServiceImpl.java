package com.lender.productservice.service.impl;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.baseservice.payload.request.FileObjectRequest;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productservice.configuration.CustomUserDetail;
import com.lender.productservice.entity.Category;
import com.lender.productservice.entity.Product;
import com.lender.productservice.entity.ProductResource;
import com.lender.productservice.exception.APIException;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.mapper.ProductMapper;
import com.lender.productservice.repository.CategoryRepository;
import com.lender.productservice.repository.ProductRepository;
import com.lender.productservice.repository.ProductResourceRepository;
import com.lender.productservice.service.ProductResourceService;
import com.lender.productservice.service.ProductService;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.payload.CategoryDto;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.PageResponseCategory;
import com.lender.productserviceshare.payload.response.PageResponseProduct;
import com.lender.productserviceshare.payload.response.ProductResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String THUMB = "imageUrl";

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductResourceService resourceService;
    private final ProductMapper productMapper;
    private final ResponseFactory responseFactory;
    private final StreamBridge streamBridge;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Product create(ProductRequest request) {

        Set<Category> categories = request.getCategoryIds().stream()
                .map(categoryId -> categoryRepository.findByIdAndStatus(categoryId, Status.ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId)))
                .collect(Collectors.toSet());

        Product product = productMapper.requestToEntity(request);
        product.setCategories(categories);
        entityManager.persist(product);
        request.getImageValues()
                .forEach(imageValue -> uploadImage(product.getId(), resourceService.initResource(product).getId(), imageValue));
        return productRepository.save(product);
    }

//    @Override
//    public ResponseEntity<BaseResponse<ProductResponse>> update(String id, ProductRequest request) {
//        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

//        Product product = productRepository.findByIdAndOwner(id, Status.ACTIVE, userDetail.getId())
//                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Không được phép truy cập"));
//
//        Set<Category> categories = request.getCategoryIds().stream()
//                .map(categoryId -> categoryRepository.findByIdAndStatus(categoryId, Status.ACTIVE)
//                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId)))
//                .collect(Collectors.toSet());
//
//        productMapper.requestToEntity(request, product);
//        product.setCategories(categories);
//        uploadImage(product.getId(), request.getImageValue());
//
//        return responseFactory.success("Sucess", productMapper.entityToResponse(productRepository.save(null)));
//    }

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
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sản phẩm", "id", id));
        ProductResponse response = productMapper.entityToResponse(product);
        response.setImageUrls(resourceService.getImageUrls(response.getId()));

        return responseFactory.success("Success", response);
    }
//
//    @Override
//    public ResponseEntity<BaseResponse<PageResponseProduct>> findAllByFilter(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
//        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
//                ? Sort.by(sortBy).ascending()
//                : Sort.by(sortBy).descending();
//
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//        Session session = entityManager.unwrap(Session.class);
//        Filter filter = session.enableFilter("deleteProductFilter");
//        filter.setParameter("status", Status.ACTIVE.toString());
//        Page<Product> productPage = productRepository.findAll(pageable);
//
//        session.disableFilter("deleteProductFilter");
//        return responseFactory.success("Success", paging(productPage));
//    }
//
//    @Override
//    public ResponseEntity<BaseResponse<PageResponseProduct>> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
//        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
//                ? Sort.by(sortBy).ascending()
//                : Sort.by(sortBy).descending();
//
//        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
//        Page<Product> productPage = productRepository.findAll(pageable);
//
//        return responseFactory.success("Success", paging(productPage));
//    }
//
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
//    private PageResponseProduct paging(Page<Product> productPage) {
//        List<ProductResponse> productResponses = productPage.getContent().stream()
//                .map(productMapper::entityToResponse)
//                .toList();
//
//        return (PageResponseProduct) PageResponseProduct.builder()
//                .pageNo(productPage.getNumber())
//                .pageSize(productResponses.size())
//                .content(productResponses)
//                .totalPages(productPage.getTotalPages())
//                .totalItems((int) productPage.getTotalElements())
//                .last(productPage.isLast())
//                .build();
//    }
}
