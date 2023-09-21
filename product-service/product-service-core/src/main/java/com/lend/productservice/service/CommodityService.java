package com.lend.productservice.service;

import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.productservice.entity.Product;
import com.lend.productserviceshare.payload.request.CommodityRequest;
import com.lend.productserviceshare.payload.response.CommodityResponse;
import com.lend.productserviceshare.payload.response.PageResponseCommodity;
import org.springframework.http.ResponseEntity;

public interface CommodityService {

    ResponseEntity<BaseResponse<CommodityResponse>> create(CommodityRequest request);

    ResponseEntity<BaseResponse<CommodityResponse>> update(String id, CommodityRequest request);

    ResponseEntity<BaseResponse<CommodityResponse>> getById(String id);

//    ResponseEntity<BaseResponse<PageResponseCommodity>> findAllByActive(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
//
//    ResponseEntity<BaseResponse<PageResponseCommodity>> findAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ResponseEntity<BaseResponse<String>> deleteById(String id);

    ResponseEntity<BaseResponse<CommodityResponse>> restore(String id);
}
