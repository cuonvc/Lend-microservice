package com.lend.authservice.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RenewPasswordRequest {

    private String code;

    @NotBlank(message = "Mật khẩu không được để trống")
    @NotNull
    @NotEmpty(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 20, message = "Mật khẩu phải chứa từ 8 đến 20 ký tự")
    private String newPassword;

    private String retypePassword;
}
