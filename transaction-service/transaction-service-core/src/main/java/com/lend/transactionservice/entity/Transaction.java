package com.lend.transactionservice.entity;

import com.lend.baseservice.constant.enumerate.Status;
import com.lend.transactionservice.enumerate.PaymentStatus;
import com.lend.transactionservice.enumerate.PaymentType;
import com.lend.transactionservice.enumerate.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Document("transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transaction {

    @Id
    private String id;

    @Field(name = "lender_id")
    private String lessorId;

    @Field(name = "borrower_id")
    private String lesseeId;

    @Field(name = "borrower_address")
    private String lesseeAddress;

    @Field(name = "product_id")
    private String commodityId;

    @Field(name = "quantity")
    private List<String> serialNumbers;  //list or single

    @Field(name = "amount")
    private Double amount;

    @Field(name = "payment_type")
    private PaymentType paymentType = PaymentType.COD;

    @Field(name = "payment_status")
    private PaymentStatus paymentStatus;

    @Field(name = "transaction_status")
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;

    @Field(name = "bill_code")
    private String billCode;

    @Field(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Field(name = "modified_date")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Field(name = "accepted_date")
    private LocalDateTime acceptedDate;

    @Field(name = "is_active")
    private Status isActive = Status.ACTIVE;
}
