package com.lender.productservice.service;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productserviceshare.payload.request.CommodityRequest;
import com.lender.productserviceshare.payload.response.CommodityResponse;
import com.lender.productserviceshare.payload.response.PageResponseCommodity;
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
