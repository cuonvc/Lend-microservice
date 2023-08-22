package com.lender.transactionservice.response;

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
public class TransactionResponseRaw {

    private String id;

    private String lenderId;

    private String borrowerId;

    private String productId;

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
