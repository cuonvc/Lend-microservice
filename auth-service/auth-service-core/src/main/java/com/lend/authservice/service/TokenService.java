package com.lend.authservice.service;

import com.lend.authservice.entity.RefreshToken;
import com.lend.authservice.entity.User;

public interface TokenService {

    boolean validateAccessToken(String token);

    void initRefreshToken(User user);

    void clearToken(String userId);

    RefreshToken generateTokenObject(User user);
}
