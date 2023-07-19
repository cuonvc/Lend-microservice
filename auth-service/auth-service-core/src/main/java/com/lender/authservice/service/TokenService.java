package com.lender.authservice.service;

import com.lender.authservice.entity.RefreshToken;
import com.lender.authservice.entity.User;

public interface TokenService {

    boolean validateAccessToken(String token);

    void initRefreshToken(User user);

    RefreshToken generateTokenObject(User user);
}
