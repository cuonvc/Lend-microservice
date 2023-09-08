package com.lender.productservice.entity;

import com.lender.baseservice.constant.enumerate.Status;
import com.lender.productserviceshare.enumerate.LendType;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.enumerate.TimeFrame;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "commodity_tbl", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"code"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@SQLDelete(sql = "UPDATE commodity_tbl SET is_active = 'INACTIVE' WHERE id=?")
@FilterDef(name = "deleteCommodityFilter",
        parameters = @ParamDef(name = "status", type = String.class))
@Filter(name = "deleteCommodityFilter", condition = "is_active = :status")
public class Commodity {

    @Id
    @GenericGenerator(name = "custom_commodity_id", strategy = "com.lender.productservice.util.CustomCommodityIdGenerator")
    @GeneratedValue(generator = "custom_commodity_id")
    private String id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private ProductState state = ProductState.OLD;

    @Column(name = "standard_price", nullable = false)
    private Double standardPrice;

    @Column(name = "sale_price")
    private Double salePrice;

    @Column(name = "sale_expire_at")
    private LocalDateTime saleExpireAt;

    @Column(name = "remaining")
    private Integer remaining = 0;

    @Column(name = "is_active")
    @Enumerated(EnumType.STRING)
    private Status isActive = Status.ACTIVE;

    @Column(name = "available_date")
    private LocalDateTime availableDate = LocalDateTime.now();

    @Column(name = "expire_date", nullable = false)
    private LocalDateTime expireDate;

    @Column(name = "time_frames")
    private String timeFrames;  //concat multiple types

    @Column(name = "transaction_methods")
    private String transactionMethods; //concat multiple methods

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "accept_area")
    private String acceptArea;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private LendType type = LendType.INDIVIDUAL;

    @Column(name = "deposit_required", nullable = false)
    private Boolean depositRequired;

    @Column(name = "deposit_amount")
    private Double depositAmount;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Column(name = "user_id")
    private String userId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
