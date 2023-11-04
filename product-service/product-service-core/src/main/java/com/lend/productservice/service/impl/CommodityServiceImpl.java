package com.lend.productservice.service.impl;

import com.lend.productservice.configuration.CustomUserDetail;
import com.lend.productservice.entity.Commodity;
import com.lend.productservice.entity.Product;
import com.lend.productservice.exception.APIException;
import com.lend.productservice.exception.ResourceNotFoundException;
import com.lend.productservice.mapper.CommodityMapper;
import com.lend.productservice.mapper.ProductMapper;
import com.lend.productservice.repository.CommodityRepository;
import com.lend.productservice.repository.ProductRepository;
import com.lend.productservice.service.CommodityService;
import com.lend.productservice.service.ProductResourceService;
import com.lend.productservice.service.ProductService;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import com.lend.productserviceshare.payload.request.CommodityRequest;
import com.lend.productserviceshare.payload.response.CommodityResponse;
import com.lend.productserviceshare.payload.response.ProductResponse;
import com.lend.productserviceshare.payload.response.SerialNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommodityServiceImpl implements CommodityService {

    private final ProductService productService;
    private final ProductResourceService productResourceService;
    private final CommodityMapper commodityMapper;
    private final CommodityRepository commodityRepository;
    private final ProductRepository productRepository;
    private final ResponseFactory responseFactory;
    private final ProductMapper productMapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public ResponseEntity<BaseResponse<CommodityResponse>> create(CommodityRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!validateUnique(request.getSerialNumbers())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Serial number không được trùng nhau");
        }

        Commodity commodity = commodityMapper.requestToEntity(request);
        commodity = commodityRepository.save(commodity);

        Product product = productService.create(commodity, request, userDetail.getId());
        commodity.setUserId(userDetail.getId());
        commodity.setProduct(product);
        commodity.setSerialNumbers(generateUUIDSet(request.getSerialNumbers()));
        CommodityResponse response = mappingResponse(commodityRepository
                .save(commodity));



        return responseFactory.success("Tạo thành công", response);
    }

    @Override
    public ResponseEntity<BaseResponse<CommodityResponse>> update(String id, CommodityRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Commodity commodity = commodityRepository.findByIdAndUserId(id, userDetail.getId())
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));
        Product product = productService.update(commodity.getProduct(), request.getProductRequest());

        commodityMapper.requestToEntity(request, commodity);
        CommodityResponse response = commodityMapper.entityToResponse(commodityRepository.save(commodity));
        response.setProduct(productMapper.entityToResponse(product));

        return responseFactory.success("Cập nhật thành công", response);
    }

    private boolean validateUnique(List<String> serialNumber) {
        Set<String> set = new HashSet<>(serialNumber);
        return serialNumber.size() == set.size();
    }

    private Set<SerialNumber> generateUUIDSet(List<String> serialNumbers) {
        return serialNumbers.stream()
//                .filter(Objects::isNull)
                .map(seri -> new SerialNumber(Optional.ofNullable(seri)
                            .orElse("s_" + UUID.randomUUID().toString().substring(0, 7)), Status.ACTIVE)
                )
                .collect(Collectors.toSet());
    }

    @Override
    public ResponseEntity<BaseResponse<CommodityResponse>> getById(String id) {
        Commodity commodity = commodityRepository.findByIdAndIsActive(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Mặt hàng", "id", id));

        return responseFactory.success("Thông tin sản phẩm", mappingResponse(commodity));
    }

    @Override
    public void setStatusSerialNumbers(String commodityId, Set<String> serialNumbers, Status status) {
        Commodity commodity = commodityRepository.findById(commodityId)
                .orElseThrow(() -> new ResourceNotFoundException("Commodity", "id", commodityId));

        commodity.getSerialNumbers().stream()
                .filter(s -> serialNumbers.contains(s.getValue()))
                .forEach(s -> s.setStatus(status));

        commodityRepository.save(commodity);
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<String>> deleteById(String id) {
        Commodity commodity = commodityRepository.findByIdAndIsActive(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Mặt hàng", "id", id));

        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetail.getGrantedAuthorities().get(0).equals("USER") && !userDetail.getId().equals(commodity.getUserId())) {
            return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Không được phép truy cập", null);
        }

        commodity.setIsActive(Status.INACTIVE);
        commodity.getProduct().setIsActive(Status.INACTIVE);
        commodityRepository.save(commodity);
        productRepository.save(commodity.getProduct());
        return responseFactory.success("Xóa thành công", "Xóa thành công");
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<CommodityResponse>> restore(String id) {
        Commodity commodity = commodityRepository.findByIdAndIsActive(id, Status.INACTIVE)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Mặt hàng đang online hoặc không tồn tại"));

        commodity.setIsActive(Status.ACTIVE);
        commodityRepository.save(commodity);

//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
//
//        Root<Product> root = criteriaQuery.from(Product.class);
//        Join<Product, Commodity> commodityJoin = root.join(Product_.commodity, JoinType.INNER);
//        criteriaQuery.where(criteriaBuilder.equal(commodityJoin.get(Commodity_.ID), id));
//
//
//
//        Product product = entityManager.createQuery(criteriaQuery).getSingleResult();
//        product.setIsActive(Status.ACTIVE);
//        entityManager.persist(product);

        Product product1 = commodity.getProduct();
        product1.setIsActive(Status.ACTIVE);
        productRepository.save(product1);

        return responseFactory.success("Khôi phục thành công", mappingResponse(commodity));
    }

    private CommodityResponse mappingResponse(Commodity commodity) {
        CommodityResponse response = commodityMapper.entityToResponse(commodity);
        ProductResponse productResponse = productMapper.entityToResponse(commodity.getProduct());
        productResponse.setResources(productResourceService.getImageUrls(productResponse.getId()));
        response.setProduct(productResponse);

        return response;
    }

}
