package com.lend.productservice.service.impl;

import com.lend.baseservice.payload.request.FileObjectRequest;
import com.lend.productservice.entity.Category;
import com.lend.productservice.mapper.CategoryMapper;
import com.lend.productservice.repository.CategoryRepository;
import com.lend.productservice.repository.custom.CategoryCustomRepository;
import com.lend.productservice.service.CategoryService;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import com.lend.productservice.exception.APIException;
import com.lend.productservice.exception.ResourceNotFoundException;
import com.lend.productserviceshare.payload.CategoryDto;
import com.lend.productserviceshare.payload.response.CategoryResponse;
import com.lend.productserviceshare.payload.response.PageResponseCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_IMAGE_REQUEST = "category-image-request";

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ResponseFactory responseFactory;
    private final CategoryCustomRepository categoryCustomRepository;
    private final StreamBridge streamBridge;

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> create(CategoryDto dto) {
        validateCategory("name", dto.getName());
        if (dto.getParentId() != null) {
            validateCategory("id", dto.getParentId());
        }

        if (!isBase64Image(dto.getImageValue())) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Base64 image invalid", null);
        }
        Category category = categoryRepository.save(categoryMapper.dtoToEntity(dto));
        CategoryResponse response = categoryMapper.entityToResponse(category);

        storeImage(category.getId(), dto.getImageValue());
        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<CategoryResponse>> update(CategoryDto categoryDto) {
        Category category = validateCategory("id", categoryDto.getId());
        validateCategory("name", categoryDto.getName());
        if (categoryDto.getParentId() != null) {
            categoryDto.setParentId(null);
        }

        if (!isBase64Image(categoryDto.getImageValue())) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Base64 image invalid", null);
        }

        categoryMapper.dtoToEntity(categoryDto, category);
        category = categoryRepository.save(category);
        CategoryResponse response = categoryMapper.entityToResponse(category);
        response.setChildren(getResponseWithChildren(category.getId()));

        storeImage(category.getId(), categoryDto.getImageValue());
        return responseFactory.success("Success", response);
    }

    private boolean isBase64Image(String data) {
        try {
            byte[] decoded = Base64.getDecoder().decode(data);
            return ImageIO.read(new ByteArrayInputStream(decoded)) != null;
        } catch (IOException e) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Base64 Image invalid");
        }
    }

    private void storeImage(String categoryId, String base64Image) {
        Message<FileObjectRequest> message = MessageBuilder
                .withPayload(FileObjectRequest.builder()
                        .fileBytes(Base64.getDecoder().decode(base64Image))
                        .build())
                .setHeader(KafkaHeaders.KEY, categoryId.getBytes())
                .build();

        streamBridge.send(CATEGORY_IMAGE_REQUEST, message);
    }

    private Category validateCategory(String field, String value) {
        return switch (field) {
            case "name" -> {
                if (categoryRepository.findByNameAndIsActive(value, Status.ACTIVE).isPresent()) {
                    throw new APIException(HttpStatus.BAD_REQUEST, "Category '" + value + "' đã tồn tại hoặc tên không hợp lệ");
                }
                yield null;
            }

            case "id" -> categoryRepository.findByIdAndIsActive(value, Status.ACTIVE)
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
        categoryRepository.findByParentIdAndIsActive(parentId, Status.ACTIVE)
                .forEach(category -> {
                    CategoryResponse response = categoryMapper.entityToResponse(category);
                    fetchChildCategory(response);
                    result.add(response);
                });

        return result;
    }

    private void fetchChildCategory(CategoryResponse response) {
        Set<CategoryResponse> children = new HashSet<>();
        categoryRepository.findByParentIdAndIsActive(response.getId(), Status.ACTIVE)
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

        Page<Category> categories = categoryRepository.findAllByIsActive(pageable, Status.ACTIVE);
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
