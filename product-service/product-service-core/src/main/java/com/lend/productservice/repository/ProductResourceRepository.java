package com.lend.productservice.repository;

import com.lend.productservice.entity.Product;
import com.lend.productservice.entity.ProductResource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductResourceRepository extends MongoRepository<ProductResource, String> {

//    @Query("SELECT r FROM ProductResource r WHERE r.product.id = :productId")
//    List<ProductResource> findByProductId(String productId);
//
//    @Modifying
//    @Query("DELETE FROM ProductResource r WHERE r.product.id = :productId")
//    void deleteByProductId(String productId);

    List<ProductResource> findByProductId(String id);
}
