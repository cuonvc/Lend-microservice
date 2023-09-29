package com.lend.productserviceshare.payload.request;

import com.lend.productserviceshare.enumerate.LendType;
import com.lend.productserviceshare.enumerate.ProductState;
import com.lend.productserviceshare.enumerate.TimeFrame;
import com.lend.productserviceshare.enumerate.TransactionMethod;
import jakarta.validation.Valid;
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
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommodityRequest {

    @NotNull
    @Valid
    private ProductRequest productRequest;

    private ProductState state;

    @NotNull
    private Double standardPrice;

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

    @NotNull
    @Size(min = 1, message = "Mặt hàng phải có ít nhất một sản phẩm!")
    private List<String> serialNumbers;
}
