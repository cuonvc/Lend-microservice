package com.lender.authservice.payload.response;

import com.lender.authservice.entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenObjectResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private RefreshToken refreshToken;
}