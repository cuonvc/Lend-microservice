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
//@Table(name = "lend_transaction_tbl")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder(toBuilder = true)
//public class LendTransaction {
//
//    @Id
//    @GenericGenerator(name = "custom_lend_id", strategy = "com.lender.transactionservice.util.CustomLendIdGenerator")
//    @GeneratedValue(generator = "custom_lend_id")
//    private String id;
//
//    @Column(name = "user_id")
//    private String userId;
//
//    @Column(name = "product_id")
//    private String productId;
//}
