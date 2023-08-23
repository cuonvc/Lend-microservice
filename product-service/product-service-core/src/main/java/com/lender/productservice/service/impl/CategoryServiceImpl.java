package com.lender.productservice.service.impl;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.productservice.entity.Category;
import com.lender.productservice.exception.APIException;
import com.lender.productservice.exception.ResourceNotFoundException;
import com.lender.productservice.mapper.CategoryMapper;
import com.lender.productservice.repository.CategoryRepository;
import com.lender.productservice.service.CategoryService;
import com.lender.productserviceshare.payload.CategoryDto;
import com.lender.productserviceshare.payload.response.PageResponseCategory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.metrics.Stat;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ResponseFactory responseFactory;
    private final EntityManager entityManager;

    @Override
    public ResponseEntity<BaseResponse<CategoryDto>> create(CategoryDto categoryDto) {
        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Category '"
                    + categoryDto.getName() + "' đã tồn tại hoặc bị xóa", null);
        }

        CategoryDto response = categoryMapper
                .entityToDto(categoryRepository.save(categoryMapper.dtoToEntity(categoryDto)));

        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryDto>> update(CategoryDto categoryDto) {
        Category category = categoryRepository.findById(categoryDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryDto.getId()));

        if (categoryRepository.findByName(categoryDto.getName()).isPresent()) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Category '" + categoryDto.getName()
                    + "' đã tồn tại hoặc bị xóa", null);
        }

        categoryMapper.dtoToEntity(categoryDto, category);
        CategoryDto response = categoryMapper.entityToDto(categoryRepository.save(category));

        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryDto>> getById(String id) {
        Category category = categoryRepository.findByIdAndStatus(id, Status.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        return responseFactory.success("Success", categoryMapper.entityToDto(category));
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponseCategory>> getAllActive(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Session session = entityManager.unwrap(Session.class);
        Filter filter = session.enableFilter("deletedCategoryFilter");
        filter.setParameter("status", Status.ACTIVE.toString());
        Page<Category> categories = categoryRepository.findAll(pageable);

        session.disableFilter("deletedCategoryFilter");
        return responseFactory.success("Success", paging(categories));
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponseCategory>> getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> categories = categoryRepository.findAll(pageable);

        return responseFactory.success("Success", paging(categories));
    }

    @Override
    public ResponseEntity<BaseResponse<String>> delete(String id) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

        categoryRepository.delete(category);
        return responseFactory.success("Success", "Đã xóa " + category.getName());
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryDto>> restore(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        category.setIsActive(Status.ACTIVE);
        category = categoryRepository.save(category);

        return responseFactory.success("Success", categoryMapper.entityToDto(category));
    }

    private PageResponseCategory paging(Page<Category> categoryPage) {
        List<CategoryDto> categoryDtos = categoryPage.getContent().stream()
                .map(categoryMapper::entityToDto)
                .toList();

        return (PageResponseCategory) PageResponseCategory.builder()
                .pageNo(categoryPage.getNumber())
                .pageSize(categoryDtos.size())
                .content(categoryDtos)
                .totalPages(categoryPage.getTotalPages())
                .totalItems((int) categoryPage.getTotalElements())
                .last(categoryPage.isLast())
                .build();
    }
}
