package com.lender.productservice.service;

import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productserviceshare.payload.CategoryDto;
import com.lender.productserviceshare.payload.response.CategoryResponse;
import com.lender.productserviceshare.payload.response.PageResponseCategory;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<BaseResponse<CategoryResponse>> create(CategoryDto categoryDto);

    ResponseEntity<BaseResponse<CategoryDto>> update(CategoryDto categoryDto);

    ResponseEntity<BaseResponse<CategoryDto>> getById(String id);

    ResponseEntity<BaseResponse<PageResponseCategory>> getAllActive(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ResponseEntity<BaseResponse<PageResponseCategory>> getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ResponseEntity<BaseResponse<String>> delete(String id);

    ResponseEntity<BaseResponse<CategoryDto>> restore(String id);
}
