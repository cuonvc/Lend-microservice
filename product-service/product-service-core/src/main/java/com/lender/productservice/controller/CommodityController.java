package com.lender.productservice.controller;

import com.lender.baseservice.constant.PageConstant;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productservice.service.CommodityService;
import com.lender.productserviceshare.payload.request.CommodityRequest;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.CommodityResponse;
import com.lender.productserviceshare.payload.response.PageResponseCommodity;
import com.lender.productserviceshare.payload.response.PageResponseProduct;
import com.lender.productserviceshare.payload.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/commodity")
@RequiredArgsConstructor
public class CommodityController {

    private final CommodityService commodityService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<CommodityResponse>> create(@Valid @RequestBody CommodityRequest request) {
        return commodityService.create(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BaseResponse<CommodityResponse>> update(@PathVariable("id") String id,
                                                                  @Valid @RequestBody CommodityRequest request) {
        return commodityService.update(id, request);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<BaseResponse<CommodityResponse>> details(@PathVariable("id") String id) {
        return commodityService.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse<String>> deleteById(@PathVariable("id") String id) {
        return commodityService.deleteById(id);
    }

    @PutMapping("/admin/restore/{id}")
    public ResponseEntity<BaseResponse<CommodityResponse>> restoreById(@PathVariable("id") String id) {
        return commodityService.restore(id);
    }
}
