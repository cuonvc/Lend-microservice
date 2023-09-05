package com.lender.productservice.service;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productserviceshare.payload.request.CommodityRequest;
import com.lender.productserviceshare.payload.response.CommodityResponse;
import org.springframework.http.ResponseEntity;

public interface CommodityService {

    ResponseEntity<BaseResponse<CommodityResponse>> create(CommodityRequest request);

    ResponseEntity<BaseResponse<CommodityResponse>> getById(String id);
}
