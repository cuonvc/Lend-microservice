package com.lend.productservice.service;

import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.productserviceshare.payload.CategoryDto;
import com.lend.productserviceshare.payload.response.CategoryResponse;
import com.lend.productserviceshare.payload.response.PageResponseCategory;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<BaseResponse<CategoryResponse>> create(CategoryDto categoryDto);

    ResponseEntity<BaseResponse<CategoryResponse>> update(CategoryDto categoryDto);

    ResponseEntity<BaseResponse<CategoryResponse>> getById(String id);

    ResponseEntity<BaseResponse<PageResponseCategory>> getAllActive(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ResponseEntity<BaseResponse<PageResponseCategory>> getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ResponseEntity<BaseResponse<String>> delete(String id);

    ResponseEntity<BaseResponse<CategoryDto>> restore(String id);
}
