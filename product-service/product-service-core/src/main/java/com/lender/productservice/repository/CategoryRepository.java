package com.lender.productservice.repository;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productservice.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findByName(String name);

    @Query("SELECT c FROM Category c WHERE c.id = :id AND c.isActive = :status")
    Optional<Category> findByIdAndStatus(String id, Status status);

    @Query("SELECT c FROM Category c WHERE c.parentId = :parentId AND c.isActive = :status")
    Set<Category> findByParentIdAndStatus(String parentId, Status status);

    @Query("SELECT c FROM Category c WHERE c.parentId = null")
    Page<Category> findAllByRoot(Pageable pageable);
}
