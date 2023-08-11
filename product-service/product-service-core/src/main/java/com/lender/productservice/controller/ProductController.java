package com.lender.productservice.controller;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.validation.ImageValid;
import com.lender.productservice.service.ProductService;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<ProductResponse>> create(@Valid @RequestBody ProductRequest request) {
        return productService.create(request);
    }

    @PostMapping("/upload/thumb/{id}")
    public ResponseEntity<BaseResponse<String>> uploadImage(@PathVariable("id") String id,
                                                            @ImageValid @RequestPart("image") MultipartFile file) {
        try {
            return productService.uploadImage(id, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> details(@PathVariable("id") String id) {
        return productService.getById(id);
    }

}
