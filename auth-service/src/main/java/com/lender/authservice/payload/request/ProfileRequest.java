package com.lender.authservice.payload.request;

import com.lender.baseservice.constant.enumerate.Gender;
import com.lender.baseservice.constant.enumerate.Role;
import com.lender.baseservice.constant.enumerate.Status;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProfileRequest {

    @NotBlank(message = "Firt name can't blank")
    @Size(min = 3, max = 6, message = "First name must be minimum 3 characters and maximum 6 characters")
    private String firstName;

    @NotBlank(message = "Last name can't blank")
    @Size(min = 3, max = 6, message = "Last name must be minimum 3 characters and maximum 6 characters")
    private String lastName;

//    @NotBlank
    private Gender gender;

    private String about;

    private String country;

    private String city;

    private String postalCode;

    private String detailAddress;
}
