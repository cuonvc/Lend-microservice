package com.lend.productservice.service.impl;

import com.lend.productservice.entity.Category;
import com.lend.productservice.mapper.CategoryMapper;
import com.lend.productservice.repository.CategoryRepository;
import com.lend.productservice.service.CategoryService;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import com.lend.productservice.exception.APIException;
import com.lend.productservice.exception.ResourceNotFoundException;
import com.lend.productserviceshare.payload.CategoryDto;
import com.lend.productserviceshare.payload.response.CategoryResponse;
import com.lend.productserviceshare.payload.response.PageResponseCategory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ResponseFactory responseFactory;
    private final EntityManager entityManager;

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> create(CategoryDto dto) {
        validateCategory("name", dto.getName());
        if (dto.getParentId() != null) {
            validateCategory("id", dto.getParentId());
        }

        Category category = categoryRepository.save(categoryMapper.dtoToEntity(dto));
        CategoryResponse response = categoryMapper.entityToResponse(category);

        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> update(CategoryDto categoryDto) {
        Category category = validateCategory("id", categoryDto.getId());
        validateCategory("name", categoryDto.getName());
        if (categoryDto.getParentId() != null) {
            categoryDto.setParentId(null);
        }

        categoryMapper.dtoToEntity(categoryDto, category);
        category = categoryRepository.save(category);
        CategoryResponse response = categoryMapper.entityToResponse(category);
        response.setChildren(getResponseWithChildren(category.getId()));

        return responseFactory.success("Success", response);
    }

    private Category validateCategory(String field, String value) {
        return switch (field) {
            case "name" -> {
                if (categoryRepository.findByName(value).isPresent()) {
                    throw new APIException(HttpStatus.BAD_REQUEST, "Category '" + value + "' đã tồn tại hoặc tên không hợp lệ");
                }
                yield null;
            }

            case "id" -> categoryRepository.findByIdAndStatus(value, Status.ACTIVE)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "id", value));

            default -> throw new APIException(HttpStatus.BAD_REQUEST, "Lỗi không xác định, liên hệ Admin");
        };
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> getById(String id) {
        Category entity = validateCategory("id", id);
        CategoryResponse response = categoryMapper.entityToResponse(entity);
        response.setChildren(getResponseWithChildren(entity.getId()));

        return responseFactory.success("Success", response);
    }

    private Set<CategoryResponse> getResponseWithChildren(String parentId) {
        Set<CategoryResponse> result = new HashSet<>();
        categoryRepository.findByParentIdAndStatus(parentId, Status.ACTIVE)
                .forEach(category -> {
                    CategoryResponse response = categoryMapper.entityToResponse(category);
                    fetchChildCategory(response);
                    result.add(response);
                });

        return result;
    }

    private void fetchChildCategory(CategoryResponse response) {
        Set<CategoryResponse> children = new HashSet<>();
        categoryRepository.findByParentIdAndStatus(response.getId(), Status.ACTIVE)
                .forEach(category -> {
                    CategoryResponse childResponse = categoryMapper.entityToResponse(category);
                    fetchChildCategory(childResponse);
                    children.add(childResponse);
                });

        response.setChildren(children);
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponseCategory>> getAllActive(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Session session = entityManager.unwrap(Session.class);
        Filter filter1 = session.enableFilter("deletedCategoryFilter");
        filter1.setParameter("status", Status.ACTIVE.toString());

        Page<Category> categories = categoryRepository.findAllByRoot(pageable);
        session.disableFilter("deletedCategoryFilter");
        session.close();
        return responseFactory.success("Success", paging(categories));
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponseCategory>> getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Category> categories = categoryRepository.findAllByRoot(pageable);

        return responseFactory.success("Success", paging(categories));
    }

    @Override
    public ResponseEntity<BaseResponse<String>> delete(String id) {
        Category category = validateCategory("id", id);

        categoryRepository.delete(category);
        return responseFactory.success("Success", "Đã xóa " + category.getName());
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryDto>> restore(String id) {
        Category category = validateCategory("id", id);
        category.setIsActive(Status.ACTIVE);
        category = categoryRepository.save(category);

        return responseFactory.success("Success", categoryMapper.entityToDto(category));
    }

    private PageResponseCategory paging(Page<Category> categoryPage) {
        List<CategoryResponse> responseList = categoryPage.getContent().stream()
                .map(entity -> {
                    CategoryResponse response = categoryMapper.entityToResponse(entity);
//                    response.setParent(categoryMapper.entityToResponse(validateCategory("id", entity.getId())));
                    response.setChildren(getResponseWithChildren(entity.getId()));
                    return response;
                })
                .toList();

        return (PageResponseCategory) PageResponseCategory.builder()
                .pageNo(categoryPage.getNumber())
                .pageSize(responseList.size())
                .content(responseList)
                .totalPages(categoryPage.getTotalPages())
                .totalItems((int) categoryPage.getTotalElements())
                .last(categoryPage.isLast())
                .build();
    }
}
