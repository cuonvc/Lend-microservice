package com.lender.productservice.repository;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productservice.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.isActive = :status")
    Optional<Product> findByIdAndStatus(String id, Status status);

}
