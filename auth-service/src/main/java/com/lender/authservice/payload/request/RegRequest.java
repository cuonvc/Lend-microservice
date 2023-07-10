package com.lender.authservice.payload.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RegRequest {

    @NotNull
    @NotBlank(message = "Firt name can't blank")
    @NotEmpty
    @Size(min = 3, max = 6, message = "First name must be minimum 3 characters and maximum 6 characters")
    private String firstName;

    @NotNull
    @NotBlank(message = "Last name can't blank")
    @NotEmpty
    @Size(min = 3, max = 6, message = "Last name must be minimum 3 characters and maximum 6 characters")
    private String lastName;

    @Email
    @NotNull
    @NotBlank(message = "Email can't blank")
    @NotEmpty
    private String email;

    @NotEmpty
    @NotNull
    @NotBlank(message = "Password can't blank")
    @Size(min = 8, max = 20, message = "Password must be minimum 8 characters and maximum 20 characters")
    private String password;

}
