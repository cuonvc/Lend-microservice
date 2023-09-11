package com.lend.productservice.controller;

import com.lend.productservice.service.CategoryService;
import com.lend.baseservice.constant.PageConstant;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.productserviceshare.payload.CategoryDto;
import com.lend.productserviceshare.payload.response.CategoryResponse;
import com.lend.productserviceshare.payload.response.PageResponseCategory;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/moderator/create")
    public ResponseEntity<BaseResponse<CategoryResponse>> create(@Valid @RequestBody CategoryDto categoryDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
        return categoryService.create(categoryDto);
    }

    //just update name and description
    @PutMapping("/moderator/{categoryId}")
    public ResponseEntity<BaseResponse<CategoryResponse>> update(@PathVariable("categoryId") String id,
                                                            @Valid @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        return categoryService.update(categoryDto);
    }

    @GetMapping("/view/{categoryId}")
    public ResponseEntity<BaseResponse<CategoryResponse>> getById(@PathVariable("categoryId") String id) {
        return categoryService.getById(id);
    }

    @GetMapping("/view/all")
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

    @GetMapping("/moderator/all")
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

    @DeleteMapping("/moderator/{categoryId}")
    public ResponseEntity<BaseResponse<String>> deleteById(@PathVariable("categoryId") String id) {
        return categoryService.delete(id);
    }

    @PostMapping("/admin/{categoryId}")
    public ResponseEntity<BaseResponse<CategoryDto>> restoreById(@PathVariable("categoryId") String id) {
        return categoryService.restore(id);
    }
}
