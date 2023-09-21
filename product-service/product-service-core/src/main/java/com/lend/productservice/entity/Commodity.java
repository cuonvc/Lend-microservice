package com.lend.productservice.entity;

import com.lend.baseservice.constant.enumerate.Status;
import com.lend.productserviceshare.enumerate.LendType;
import com.lend.productserviceshare.enumerate.ProductState;
import com.lend.productserviceshare.enumerate.TimeFrame;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document("commodity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Commodity {

    @Id
    private String id;

    private String code;

    private ProductState state = ProductState.OLD;

    @Field("standard_price")
    private Double standardPrice;

    @Field("sale_price")
    private Double salePrice;

    @Field("sale_expire_at")
    private LocalDateTime saleExpireAt;

    private Integer remaining = 0;

    @Field("is_active")
    private Status isActive = Status.ACTIVE;

    @Field("available_date")
    private LocalDateTime availableDate = LocalDateTime.now();

    @Field("expire_date")
    private LocalDateTime expireDate;

    @Field("time_frames")
    private String timeFrames;  //concat multiple types

    @Field("transaction_methods")
    private String transactionMethods; //concat multiple methods

    private String location;

    @Field("accept_area")
    private String acceptArea;

    private LendType type = LendType.INDIVIDUAL;

    @Field("deposit_required")
    private Boolean depositRequired;

    @Field("deposit_amount")
    private Double depositAmount;

    @Field("created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Field("modified_date")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Field("user_id")
    private String userId;

    @Field("product")
    private Product product;

    @Field("serial_numbers")
    private Set<String> serialNumbers = new HashSet<>();
}
