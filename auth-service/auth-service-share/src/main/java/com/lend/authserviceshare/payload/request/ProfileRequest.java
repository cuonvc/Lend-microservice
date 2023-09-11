package com.lend.authserviceshare.payload.request;

import com.lend.authserviceshare.payload.enumerate.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Size(min = 1, max = 100, message = "About me must be minimum 1 characters and maximum 100 characters")
    private String about;

    private String country;

    private String city;

    private String postalCode;

    private String detailAddress;
}
