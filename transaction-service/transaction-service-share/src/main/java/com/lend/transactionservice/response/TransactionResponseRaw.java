package com.lend.transactionservice.response;

import com.lend.transactionservice.enumerate.PaymentStatus;
import com.lend.transactionservice.enumerate.TransactionStatus;
import com.lend.transactionservice.enumerate.PaymentType;
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

    private String lessorId;

    private String lesseeId;

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
