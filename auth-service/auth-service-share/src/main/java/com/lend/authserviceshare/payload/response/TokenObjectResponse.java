package com.lend.authserviceshare.payload.response;

import com.lend.authserviceshare.payload.dto.RefreshTokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenObjectResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private RefreshTokenDto refreshToken;
}