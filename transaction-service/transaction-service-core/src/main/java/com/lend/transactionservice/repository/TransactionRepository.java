package com.lend.transactionservice.repository;

import com.lend.transactionservice.entity.Transaction;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.transactionservice.enumerate.TransactionStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Optional<Transaction> findByIdAndIsActive(String id, Status status);

    List<Transaction> findAllByLesseeIdAndTransactionStatus(String lesseeId, TransactionStatus status);

    List<Transaction> findAllByLessorId(String id);

//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Transactional
//    @Query("UPDATE Transaction t SET t.transactionStatus = :status WHERE t.id = :id")
//    void updateStatus(String id, TransactionStatus status);
}
