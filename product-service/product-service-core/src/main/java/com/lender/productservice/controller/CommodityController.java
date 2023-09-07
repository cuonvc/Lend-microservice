package com.lender.productservice.controller;

import com.lender.baseservice.constant.PageConstant;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productservice.service.CommodityService;
import com.lender.productserviceshare.payload.request.CommodityRequest;
import com.lender.productserviceshare.payload.request.ProductRequest;
import com.lender.productserviceshare.payload.response.CommodityResponse;
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

//    @PostMapping("/upload/thumb/{id}")
//    public ResponseEntity<BaseResponse<String>> uploadImage(@PathVariable("id") String id,
//                                                            @RequestPart("imageValue") String file) {
//        try {
//            return productService.uploadImage(id, file);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @GetMapping("/view/{id}")
    public ResponseEntity<BaseResponse<CommodityResponse>> details(@PathVariable("id") String id) {
        return commodityService.getById(id);
    }

//    @GetMapping("/view/all")
//    public ResponseEntity<BaseResponse<PageResponseProduct>> getAllActive(@RequestParam(value = "pageNo",
//            defaultValue = PageConstant.PAGE_NO, required = false) Integer pageNo,
//                                                                          @RequestParam(value = "pageSize",
//                                                                                  defaultValue = PageConstant.PAGE_SIZE, required = false) Integer pageSize,
//                                                                          @RequestParam(value = "sortBy",
//                                                                                  defaultValue = PageConstant.SORT_BY, required = false) String sortBy,
//                                                                          @RequestParam(value = "sortDir",
//                                                                                  defaultValue = PageConstant.SORT_DIR, required = false) String sortDir) {
//        return productService.findAllByFilter(pageNo, pageSize, sortBy, sortDir);
//    }

//    @GetMapping("/moderator/all")
//    public ResponseEntity<BaseResponse<PageResponseProduct>> getAll(@RequestParam(value = "pageNo",
//            defaultValue = PageConstant.PAGE_NO, required = false) Integer pageNo,
//                                                                    @RequestParam(value = "pageSize",
//                                                                            defaultValue = PageConstant.PAGE_SIZE, required = false) Integer pageSize,
//                                                                    @RequestParam(value = "sortBy",
//                                                                            defaultValue = PageConstant.SORT_BY, required = false) String sortBy,
//                                                                    @RequestParam(value = "sortDir",
//                                                                            defaultValue = PageConstant.SORT_DIR, required = false) String sortDir) {
//        return productService.findAll(pageNo, pageSize, sortBy, sortDir);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<BaseResponse<String>> deleteById(@PathVariable("id") String id) {
//        return productService.delete(id);
//    }
//
//    @PutMapping("/admin/restore/{id}")
//    public ResponseEntity<BaseResponse<ProductResponse>> restoreById(@PathVariable("id") String id) {
//        return productService.restore(id);
//    }
}
