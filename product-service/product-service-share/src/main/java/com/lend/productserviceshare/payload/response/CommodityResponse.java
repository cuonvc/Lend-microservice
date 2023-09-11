package com.lend.productserviceshare.payload.response;

import com.lend.productserviceshare.enumerate.LendType;
import com.lend.productserviceshare.enumerate.ProductState;
import com.lend.productserviceshare.enumerate.TimeFrame;
import com.lend.productserviceshare.enumerate.TransactionMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommodityResponse {

    private String id;

    private String code;

    private ProductState state;

    private Double standardPrice;

    private Integer remaining;

    private LocalDateTime availableDate;

    private LocalDateTime expireDate;

    private List<TimeFrame> timeFrames;

    private List<TransactionMethod> transactionMethods;

    private String location;

    private String acceptArea;

    private LendType type;

    private boolean depositRequired;

    private Double depositAmount;

    private ProductResponse productResponse;
}
