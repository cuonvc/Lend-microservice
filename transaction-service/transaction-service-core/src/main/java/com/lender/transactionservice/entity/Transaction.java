package com.lender.transactionservice.entity;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.transactionservice.enumerate.PaymentStatus;
import com.lender.transactionservice.enumerate.PaymentType;
import com.lender.transactionservice.enumerate.TransactionStatus;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_tbl", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"bill_code"})
})
@SQLDelete(sql = "UPDATE transaction_tbl SET is_active = 'INACTIVE' WHERE id=?")
@FilterDef(name = "deleteTransactionFilter",
        parameters = @ParamDef(name = "status", type = String.class))
@Filter(name = "deleteTransactionFilter", condition = "is_active = :status")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Transaction {

    @Id
    @GenericGenerator(name = "custom_trans_id", strategy = "com.lender.transactionservice.util.CustomTransactionIdGenerator")
    @GeneratedValue(generator = "custom_trans_id")
    private String id;

    @Column(name = "lender_id", nullable = false)
    private String lenderId;

    @Column(name = "borrower_id", nullable = false)
    private String borrowerId;

    @Column(name = "borrower_address", nullable = false, columnDefinition = "TEXT")
    private String borrowerAddress;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "payment_type")
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType = PaymentType.COD;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus = TransactionStatus.PENDING;

    @Column(name = "bill_code", nullable = false)
    private String billCode;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Column(name = "accepted_date")
    private LocalDateTime acceptedDate;

    @Column(name = "is_active")
    @Enumerated(EnumType.STRING)
    private Status isActive = Status.ACTIVE;
}
