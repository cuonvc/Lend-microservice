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

    @NotBlank(message = "Mật khẩu cũ không được để trống")
    @NotNull
    @NotEmpty(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    @NotNull
    @NotEmpty(message = "Mật khẩu mới không được để trống")
    @Size(min = 8, max = 20, message = "Mật khẩu phải chứa từ 8 đến 20 ký tự")
    private String newPassword;

    private String retypePassword;
}
