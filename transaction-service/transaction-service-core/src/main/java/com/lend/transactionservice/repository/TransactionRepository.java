package com.lend.transactionservice.repository;

import com.lend.transactionservice.entity.Transaction;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.transactionservice.enumerate.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("SELECT t from Transaction t WHERE t.id = :id AND t.isActive = :status")
    Optional<Transaction> findByIdAndStatus(String id, Status status);

    @Query("SELECT t from Transaction t WHERE t.borrowerId = :id AND t.transactionStatus = :status AND t.isActive = 'ACTIVE'")
    List<Transaction> findByBorrowerAndStatus(String id, TransactionStatus status);

    @Query("SELECT t FROM Transaction t WHERE t.lenderId = :id AND t.isActive = 'ACTIVE'")
    List<Transaction> findByLender(String id);

//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Transactional
//    @Query("UPDATE Transaction t SET t.transactionStatus = :status WHERE t.id = :id")
//    void updateStatus(String id, TransactionStatus status);
}
