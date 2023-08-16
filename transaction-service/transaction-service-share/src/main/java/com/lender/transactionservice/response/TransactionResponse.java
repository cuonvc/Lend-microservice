package com.lender.transactionservice.response;

import com.lender.transactionservice.enumerate.PaymentStatus;
import com.lender.transactionservice.enumerate.PaymentType;
import com.lender.transactionservice.enumerate.TransactionStatus;

import java.time.LocalDateTime;

public class TransactionResponse {

    private String id;

    private String lenderName;

    private String borrowerName;

    private String borrowerAddress;

    private String productName;

    private Integer quantity;

    private Double amount;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    private TransactionStatus transactionStatus;

    private String billCode;

    private LocalDateTime transactionDate;
}
