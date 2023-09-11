package com.lend.productservice.service.impl;

import com.lend.productservice.configuration.CustomUserDetail;
import com.lend.productservice.entity.Commodity;
import com.lend.productservice.entity.Commodity_;
import com.lend.productservice.entity.Product;
import com.lend.productservice.entity.Product_;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final EntityManager entityManager;

    @Override
    public ResponseEntity<BaseResponse<CommodityResponse>> create(CommodityRequest request) {
        Product product = productService.create(request.getProductRequest());

        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Commodity commodity = commodityMapper.requestToEntity(request);
        commodity.setUserId(userDetail.getId());
        commodity.setProduct(product);
        CommodityResponse response = commodityMapper
                .entityToResponse(commodityRepository
                        .save(commodity));
        response.setProductResponse(productMapper.entityToResponse(product));

        return responseFactory.success("Tạo thành công", response);
    }

    @Override
    public ResponseEntity<BaseResponse<CommodityResponse>> update(String id, CommodityRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Commodity commodity = commodityRepository.findByIdAndOwner(id, userDetail.getId())
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Không được phép truy cập"));
        Product product = productService.update(commodity.getProduct(), request.getProductRequest());

        commodityMapper.requestToEntity(request, commodity);
        CommodityResponse response = commodityMapper.entityToResponse(commodityRepository.save(commodity));
        response.setProductResponse(productMapper.entityToResponse(product));

        return responseFactory.success("Cập nhật thành công", response);
    }

    @Override
    public ResponseEntity<BaseResponse<CommodityResponse>> getById(String id) {
        Commodity commodity = commodityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commodity", "id", id));

        return responseFactory.success("Thông tin sản phẩm", mappingResponse(commodity));
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<String>> deleteById(String id) {
        Commodity commodity = commodityRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Mặt hàng", "id", id));

        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetail.getGrantedAuthorities().get(0).equals("USER") && !userDetail.getId().equals(commodity.getUserId())) {
            return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Không được phép truy cập", null);
        }

        commodityRepository.delete(commodity);
        productRepository.delete(commodity.getProduct());
        return responseFactory.success("Xóa thành công", "Xóa thành công");
    }

    @Override
    @Transactional
    public ResponseEntity<BaseResponse<CommodityResponse>> restore(String id) {
        Commodity commodity = commodityRepository.findByIdAndStatus(id, Status.INACTIVE)
                .orElseThrow(() -> new APIException(HttpStatus.BAD_REQUEST, "Mặt hàng đang online hoặc không tồn tại"));

        commodity.setIsActive(Status.ACTIVE);
        entityManager.persist(commodity);

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);

        Root<Product> root = criteriaQuery.from(Product.class);
        Join<Product, Commodity> commodityJoin = root.join(Product_.commodity, JoinType.INNER);
        criteriaQuery.where(criteriaBuilder.equal(commodityJoin.get(Commodity_.ID), id));

        Product product = entityManager.createQuery(criteriaQuery).getSingleResult();
        product.setIsActive(Status.ACTIVE);
        entityManager.persist(product);

        return responseFactory.success("Khôi phục thành công", mappingResponse(commodity));
    }

    private CommodityResponse mappingResponse(Commodity commodity) {
        CommodityResponse response = commodityMapper.entityToResponse(commodity);
        ProductResponse productResponse = productMapper.entityToResponse(commodity.getProduct());
        productResponse.setResources(productResourceService.getImageUrls(productResponse.getId()));
        response.setProductResponse(productResponse);

        return response;
    }

}