package com.lender.productservice.controller;

import com.lender.baseservice.constant.PageConstant;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.validation.ImageValid;
import com.lender.productservice.service.ProductService;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.PageResponseProduct;
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

    @GetMapping("/view/{id}")
    public ResponseEntity<BaseResponse<ProductResponse>> details(@PathVariable("id") String id) {
        return productService.getById(id);
    }

    @GetMapping("/view/all")
    public ResponseEntity<BaseResponse<PageResponseProduct>> getAllActive(@RequestParam(value = "pageNo",
                                                                                  defaultValue = PageConstant.PAGE_NO, required = false) Integer pageNo,
                                                                          @RequestParam(value = "pageSize",
                                                                                  defaultValue = PageConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                          @RequestParam(value = "sortBy",
                                                                                  defaultValue = PageConstant.SORT_BY, required = false) String sortBy,
                                                                          @RequestParam(value = "sortDir",
                                                                                  defaultValue = PageConstant.SORT_DIR, required = false) String sortDir) {
        return productService.findAllByActive(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/moderator/all")
    public ResponseEntity<BaseResponse<PageResponseProduct>> getAll(@RequestParam(value = "pageNo",
            defaultValue = PageConstant.PAGE_NO, required = false) Integer pageNo,
                                                                          @RequestParam(value = "pageSize",
                                                                                  defaultValue = PageConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                          @RequestParam(value = "sortBy",
                                                                                  defaultValue = PageConstant.SORT_BY, required = false) String sortBy,
                                                                          @RequestParam(value = "sortDir",
                                                                                  defaultValue = PageConstant.SORT_DIR, required = false) String sortDir) {
        return productService.findAll(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("")
    public ResponseEntity<BaseResponse<PageResponseProduct>> getAll(@RequestParam(value = "owner") String id,
                                                                    ) {

    }

}
