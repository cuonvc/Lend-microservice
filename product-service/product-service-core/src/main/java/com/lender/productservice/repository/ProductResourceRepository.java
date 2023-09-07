package com.lender.productservice.repository;

import com.lender.productservice.entity.Product;
import com.lender.productservice.entity.ProductResource;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductResourceRepository extends JpaRepository<ProductResource, String> {

    @Query("SELECT r FROM ProductResource r WHERE r.product.id = :productId")
    List<ProductResource> findByProductId(String productId);

    @Modifying
    @Query("DELETE FROM ProductResource r WHERE r.product.id = :productId")
    void deleteByProductId(String productId);
}
