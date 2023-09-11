package com.lend.authserviceshare.payload.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotNull
    @NotBlank(message = "Email can't blank")
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotBlank(message = "Password can't blank")
    @NotEmpty
    @Size(min = 8, max = 20, message = "Password must be minimum 8 characters and maximum 20 characters")
    private String password;

}
