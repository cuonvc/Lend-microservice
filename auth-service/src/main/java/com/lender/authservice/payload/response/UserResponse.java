package com.lender.authservice.payload.response;

import com.lender.baseservice.constant.enumerate.Gender;
import com.lender.baseservice.constant.enumerate.Role;
import com.lender.baseservice.constant.enumerate.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private Gender gender;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Status status;
}
