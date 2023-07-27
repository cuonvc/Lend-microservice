package com.lender.authservice.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RenewPasswordRequest {

    private String code;

    @NotBlank(message = "password is blank")
    @NotNull(message = "Password is null")
    @NotEmpty(message = "Password is empty")
    @Size(min = 8, max = 20, message = "Password must be minimum 8 characters and maximum 20 character")
    private String newPassword;

    private String retypePassword;
}
