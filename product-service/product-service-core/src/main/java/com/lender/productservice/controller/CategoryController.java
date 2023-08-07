package com.lender.productservice.controller;

import com.lender.baseservice.constant.PageConstant;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.productservice.service.CategoryService;
import com.lender.productserviceshare.payload.CategoryDto;
import com.lender.productserviceshare.payload.response.PageResponseCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/moderator")
    public ResponseEntity<BaseResponse<CategoryDto>> create(@Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.create(categoryDto);
    }

    @PutMapping("/moderator/{categoryId}")
    public ResponseEntity<BaseResponse<CategoryDto>> update(@PathVariable("categoryId") String id,
                                                            @Valid @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        return categoryService.update(categoryDto);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<BaseResponse<CategoryDto>> getById(@PathVariable("categoryId") String id) {
        return categoryService.getById(id);
    }

    @GetMapping("/list")
    public ResponseEntity<BaseResponse<PageResponseCategory>> getAllActive(@RequestParam(value = "pageNo",
                                                         defaultValue = PageConstant.PAGE_NO, required = false) Integer pageNo,
                                                                     @RequestParam(value = "pageSize",
                                                         defaultValue = PageConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                     @RequestParam(value = "sortBy",
                                                         defaultValue = PageConstant.SORT_BY, required = false) String sortBy,
                                                                     @RequestParam(value = "sortDir",
                                                         defaultValue = PageConstant.SORT_DIR, required = false) String sortDir) {
        return categoryService.getAllActive(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/moderator/list")
    public ResponseEntity<BaseResponse<PageResponseCategory>> getAll(@RequestParam(value = "pageNo",
                                                         defaultValue = PageConstant.PAGE_NO, required = false) Integer pageNo,
                                                                     @RequestParam(value = "pageSize",
                                                         defaultValue = PageConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                     @RequestParam(value = "sortBy",
                                                         defaultValue = PageConstant.SORT_BY, required = false) String sortBy,
                                                                     @RequestParam(value = "sortDir",
                                                         defaultValue = PageConstant.SORT_DIR, required = false) String sortDir) {
        return categoryService.getAll(pageNo, pageSize, sortBy, sortDir);
    }

    @DeleteMapping("/admin/{categoryId}")
    public ResponseEntity<BaseResponse<String>> deleteById(@PathVariable("categoryId") String id) {
        return categoryService.delete(id);
    }

    @PostMapping("/moderator/{categoryId}")
    public ResponseEntity<BaseResponse<CategoryDto>> restoreById(@PathVariable("categoryId") String id) {
        return categoryService.restore(id);
    }
}
