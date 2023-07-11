package com.lender.authservice.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "refresh_token_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RefreshToken {

    @Id
    @GenericGenerator(name = "custom_token_id", strategy = "com.lender.authservice.util.CustomTokenIdGenerator")
    @GeneratedValue(generator = "custom_token_id")
    private String id;

    @Column(name = "token", columnDefinition = "TEXT")
    private String token;

    @Column(name = "expire_date")
    private Date expireDate;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
