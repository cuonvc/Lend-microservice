package com.lender.productserviceshare.payload.response;

import com.lender.productserviceshare.enumerate.LendType;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.enumerate.TimeFrame;
import com.lender.productserviceshare.enumerate.TransactionMethod;
import com.lender.productserviceshare.payload.request.ProductRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
