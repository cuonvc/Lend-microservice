package com.lend.productservice.repository;

import com.lend.productservice.entity.Commodity;
import com.lend.baseservice.constant.enumerate.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommodityRepository extends MongoRepository<Commodity, String> {

//    @Query("SELECT c FROM Commodity c WHERE c.id = :id AND c.userId = :userId")
//    Optional<Commodity> findByIdAndOwner(String id, String userId);

    Optional<Commodity> findByIdAndIsActive(String id, Status status);

    Optional<Commodity> findByIdAndUserId(String id, String userId);
}
