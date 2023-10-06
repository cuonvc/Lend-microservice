package com.lend.transactionservice.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TransactionRequest {

    @NotNull
    @NotBlank
    @NotEmpty
    private String commodityId;

    @NotNull
    @Size(min = 1, message = "Số luọng sản phẩm phải lớn hơn 0")
    private List<String> serialNumbers;

    @NotNull
    @NotBlank
    @NotEmpty
    private String lesseeAddress;
}
