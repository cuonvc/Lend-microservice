package com.lend.transactionservice.response;

import com.lend.transactionservice.enumerate.PaymentStatus;
import com.lend.transactionservice.enumerate.PaymentType;
import com.lend.transactionservice.enumerate.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionResponseView {

    private String id;

    private String lessorName;

    private String lesseeName;

    private String lesseeAddress;

    private String productName;

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
