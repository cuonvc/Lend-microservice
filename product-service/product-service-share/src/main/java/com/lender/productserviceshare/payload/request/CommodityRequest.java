package com.lender.productserviceshare.payload.request;

import com.lender.productserviceshare.enumerate.LendType;
import com.lender.productserviceshare.enumerate.ProductState;
import com.lender.productserviceshare.enumerate.TimeFrame;
import com.lender.productserviceshare.enumerate.TransactionMethod;
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
public class CommodityRequest {

    @NotNull
    private ProductRequest productRequest;

    private ProductState state;

    @NotNull
    private Double standardPrice;

    private Integer remaining;

    @NotNull
    private LocalDateTime availableDate;

    @NotNull
    private LocalDateTime expireDate;

    @Size(min = 1)
    private List<TimeFrame> timeFrames;

    @Size(min = 1)
    private List<TransactionMethod> transactionMethods;

    @NotNull
    @NotBlank
    @NotEmpty
    private String location;

    @NotNull
    @NotBlank
    @NotEmpty
    private String acceptArea;

    @NotNull
    private LendType type;

    @NotNull
    private boolean depositRequired;

    private Double depositAmount;
}
