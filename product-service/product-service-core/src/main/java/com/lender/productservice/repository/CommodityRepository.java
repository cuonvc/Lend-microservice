package com.lender.productservice.repository;

import com.lender.productservice.entity.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, String> {

    @Query("SELECT c FROM Commodity c WHERE c.id = :id AND c.userId = :userId")
    Optional<Commodity> findByIdAndOwner(String id, String userId);
}
