package com.lender.productservice.service.impl;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productservice.configuration.CustomUserDetail;
import com.lender.productservice.entity.Commodity;
import com.lender.productservice.entity.Product;
import com.lender.productservice.exception.APIException;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.mapper.CommodityMapper;
import com.lender.productservice.mapper.ProductMapper;
import com.lender.productservice.repository.CommodityRepository;
import com.lender.productservice.repository.ProductResourceRepository;
import com.lender.productservice.service.CommodityService;
import com.lender.productservice.service.ProductResourceService;
import com.lender.productservice.service.ProductService;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.enumerate.TimeFrame;
import com.lender.productserviceshare.enumerate.TransactionMethod;
import com.lender.productserviceshare.payload.request.CommodityRequest;
import com.lender.productserviceshare.payload.response.CommodityResponse;
import com.lender.productserviceshare.payload.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CommodityServiceImpl implements CommodityService {

    private final ProductService productService;
    private final ProductResourceService productResourceService;
    private final CommodityMapper commodityMapper;
    private final CommodityRepository commodityRepository;
    private final ResponseFactory responseFactory;
    private final ProductMapper productMapper;

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

        CommodityResponse response = commodityMapper.entityToResponse(commodity);
        ProductResponse productResponse = productMapper.entityToResponse(commodity.getProduct());
        productResponse.setResources(productResourceService.getImageUrls(productResponse.getId()));
        response.setProductResponse(productResponse);
        return responseFactory.success("Success", response);
    }
}
