package com.lender.authserviceshare.payload.response;

import com.lender.authserviceshare.payload.enumerate.Gender;
import com.lender.authserviceshare.payload.enumerate.Role;
import com.lender.baseservice.constant.enumerate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String avatarUrl;
    private String coverUrl;
    private String about;
    private String country;
    private String city;
    private String detailAddress;
    private Role role;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Status status;
}
