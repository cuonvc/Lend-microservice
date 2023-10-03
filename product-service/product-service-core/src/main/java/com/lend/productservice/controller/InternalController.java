package com.lend.productservice.controller;

import com.lend.productservice.entity.Commodity;
import com.lend.productservice.entity.Product;
import com.lend.productservice.mapper.ProductMapper;
import com.lend.productservice.repository.ProductRepository;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import com.lend.productservice.service.CommodityService;
import com.lend.productserviceshare.payload.response.CommodityResponse;
import com.lend.productserviceshare.payload.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/internal")
@RequiredArgsConstructor
public class InternalController {

    private final CommodityService commodityService;

    @GetMapping("/commodity/{id}")
    public ResponseEntity<BaseResponse<CommodityResponse>> getDetail(@PathVariable("id") String id) {
        return commodityService.getById(id);
    }
}
