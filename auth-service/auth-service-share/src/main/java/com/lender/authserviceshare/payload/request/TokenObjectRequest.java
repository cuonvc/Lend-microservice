package com.lender.authserviceshare.payload.request;

import lombok.Data;

@Data
public class TokenObjectRequest {
    private String refreshToken;
}
