package com.lender.authservice.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PasswordChangeRequest {

    @NotBlank(message = "Old password is blank")
    @NotNull(message = "Old password is null")
    @NotEmpty(message = "Old password is empty")
    private String oldPassword;

    @NotBlank(message = "New assword is blank")
    @NotNull(message = "New password is null")
    @NotEmpty(message = "New password is empty")
    @Size(min = 8, max = 20, message = "Password must be minimum 8 characters and maximum 20 character")
    private String newPassword;

    private String retypePassword;
}
