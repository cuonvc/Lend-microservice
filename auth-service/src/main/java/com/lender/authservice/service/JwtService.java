package com.lender.authservice.service;

public interface JwtService {

    boolean validateToken(String token);
}
