package com.lend.productservice.repository;

import com.lend.productservice.entity.ProductResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductResourceRepository extends JpaRepository<ProductResource, String> {

    @Query("SELECT r FROM ProductResource r WHERE r.product.id = :productId")
    List<ProductResource> findByProductId(String productId);

    @Modifying
    @Query("DELETE FROM ProductResource r WHERE r.product.id = :productId")
    void deleteByProductId(String productId);
}
