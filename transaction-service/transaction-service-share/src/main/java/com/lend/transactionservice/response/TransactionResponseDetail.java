package com.lend.transactionservice.response;

import com.lend.transactionservice.enumerate.PaymentStatus;
import com.lend.transactionservice.enumerate.PaymentType;
import com.lend.transactionservice.enumerate.TransactionStatus;
import com.lend.authserviceshare.payload.response.UserResponse;
import com.lend.productserviceshare.payload.response.ProductResponse;
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

    private UserResponse lessor;

    private UserResponse lessee;

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
