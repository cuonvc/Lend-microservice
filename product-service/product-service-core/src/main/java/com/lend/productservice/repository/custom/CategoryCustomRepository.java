package com.lend.productservice.repository.custom;

import com.lend.baseservice.constant.enumerate.Status;
import com.lend.productservice.entity.Category;
import com.lend.productservice.entity.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CategoryCustomRepository {
    Category findByName(String name);

    Category findByIdAndStatus(String id, Status status);

    List<Category> findByParentIdAndStatus(String id, Status status);
}
