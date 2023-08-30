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
import com.lender.productserviceshare.payload.response.CategoryResponse;
import com.lender.productserviceshare.payload.response.PageResponseCategory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.metrics.Stat;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.Restriction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ResponseFactory responseFactory;
    private final EntityManager entityManager;

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> create(CategoryDto categoryDto) {
        validateCategory("name", categoryDto.getName());
        if (categoryDto.getParentId() != null) {
            validateCategory("id", categoryDto.getParentId());
        }

        Category category = categoryRepository.save(categoryMapper.dtoToEntity(categoryDto));
        CategoryResponse response = categoryMapper.entityToResponse(category);

        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryDto>> update(CategoryDto categoryDto) {
        Category category = validateCategory("id", categoryDto.getId());
        validateCategory("name", categoryDto.getName());
        if (categoryDto.getParentId() != null) {
            validateCategory("id", categoryDto.getParentId());
            if (category.getId().equals(categoryDto.getParentId())) {
                return responseFactory.fail(HttpStatus.BAD_REQUEST, "Một category không thể có hai cấp", null);
            }
        }

        categoryMapper.dtoToEntity(categoryDto, category);
        CategoryDto response = categoryMapper.entityToDto(categoryRepository.save(category));

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
        fetchChildCategory(entity, response);

        return responseFactory.success("Success", response);
    }

    private void fetchChildCategory(Category entity, CategoryResponse response) {
        categoryRepository
                .findByParentIdAndStatus(entity.getId(), Status.ACTIVE)
                .ifPresent(childEntity -> {
                    response.setChild(categoryMapper.entityToResponse(childEntity));
                    fetchChildCategory(childEntity, response.getChild());
                });
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
                    fetchChildCategory(entity, response);
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
