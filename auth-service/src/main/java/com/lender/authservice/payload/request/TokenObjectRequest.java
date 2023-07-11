package com.lender.authservice.payload.request;

import lombok.Data;

@Data
public class TokenObjectRequest {
    private String refreshToken;
}
