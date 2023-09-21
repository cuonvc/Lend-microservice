package com.lend.productservice.repository;

import com.lend.productservice.entity.Product;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.productservice.entity.ProductResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    Optional<Product> findByIdAndIsActive(String id, Status status);

    Page<Product> findByIsActive(Pageable pageable, Status status);

    Optional<Product> findByResources(ProductResource productResource);

//    @Query("UPDATE Product p SET p.isActive = :status WHERE p.commodity.id = :id")
//    @Modifying
//    void updateStatusByParentId(String id, Status status);
//
//    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isActive = :status AND p.userId = :userId")
//    Optional<Product> findByIdAndOwner(String id, Status status, String userId);

}
