package com.lender.transactionservice.entity;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.transactionservice.enumerate.PaymentStatus;
import com.lender.transactionservice.enumerate.PaymentType;
import com.lender.transactionservice.enumerate.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_tbl", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"bill_code"})
})
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
    private PaymentType paymentType = PaymentType.COD;

    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "transaction_status", nullable = false)
    private TransactionStatus transactionStatus;

    @Column(name = "bill_code", nullable = false)
    private String billCode;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate = LocalDateTime.now();
}
