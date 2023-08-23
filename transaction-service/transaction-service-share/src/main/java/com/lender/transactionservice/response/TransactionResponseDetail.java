package com.lender.transactionservice.response;

import com.lender.authserviceshare.payload.response.UserResponse;
import com.lender.productserviceshare.payload.response.ProductResponse;
import com.lender.transactionservice.enumerate.PaymentStatus;
import com.lender.transactionservice.enumerate.PaymentType;
import com.lender.transactionservice.enumerate.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionResponseDetail {

    private String id;

    private UserResponse lender;

    private UserResponse borrower;

    private ProductResponse product;

    private Integer quantity;

    private Double amount;

    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    private TransactionStatus transactionStatus;

    private String billCode;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private LocalDateTime acceptedDate;
}
