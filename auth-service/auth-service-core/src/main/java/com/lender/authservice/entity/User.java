package com.lender.authservice.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lender.authserviceshare.payload.enumerate.Gender;
import com.lender.authserviceshare.payload.enumerate.Role;
import com.lender.authserviceshare.payload.enumerate.UserProvider;
import com.lender.baseservice.constant.enumerate.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table(name = "user_tbl", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"email"})
})
public class User {

    @Id
    @GenericGenerator(name = "custom_user_id", strategy = "com.lender.authservice.util.CustomUserIdGenerator")
    @GeneratedValue(generator = "custom_user_id")
    private String id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.UNDEFINE;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(name = "cover_url", columnDefinition = "TEXT")
    private String coverUrl;

    @Column(name = "about", columnDefinition = "TEXT")
    private String about;

    @Column(name = "country")
    private String country;

    @Column(name = "province")
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "detail_address", columnDefinition = "TEXT")
    private String detailAddress;

    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate = LocalDateTime.now();

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @Column(name = "user_provider")
    @Enumerated(EnumType.STRING)
    private UserProvider provider = UserProvider.SYSTEM;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private RefreshToken refreshToken;
}
