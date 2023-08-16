//package com.lender.transactionservice.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.GenericGenerator;
//
//@Entity
//@Table(name = "borrow_transaction_tbl")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder(toBuilder = true)
//public class BorrowTransaction {
//
//    @Id
//    @GenericGenerator(name = "custom_borrow_id", strategy = "com.lender.transactionservice.util.CustomBorrowIdGenerator")
//    @GeneratedValue(generator = "custom_borrow_id")
//    private String id;
//
//    @Column(name = "user_id")
//    private String userId;
//
//    @Column(name = "product_id")
//    private String productId;
//}
