package com.lender.authservice.service.impl;

import com.lender.authservice.config.jwt.JwtTokenProvider;
import com.lender.authservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtTokenProvider tokenProvider;

    @Override
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }
}
