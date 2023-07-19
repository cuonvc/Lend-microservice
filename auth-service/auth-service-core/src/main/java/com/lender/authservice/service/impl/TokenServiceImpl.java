package com.lender.authservice.service.impl;

import com.lender.authservice.config.jwt.JwtTokenProvider;
import com.lender.authservice.entity.RefreshToken;
import com.lender.authservice.entity.User;
import com.lender.authservice.repository.RefreshTokenRepository;
import com.lender.authservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository repository;
    private static final long EXPIRE_DATE = 604800000; //mili-seconds

    @Override
    public boolean validateAccessToken(String token) {
        return tokenProvider.validateToken(token);
    }

    @Override
    public void initRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .token("")
                .user(user)
                .build();

        repository.save(refreshToken);
    }

    @Override
    public RefreshToken generateTokenObject(User user) {
        Date currentDate = new Date();
        Date expire = new Date(currentDate.getTime() + EXPIRE_DATE);
        Optional<RefreshToken> refreshToken = repository.getByUserId(user.getId());

        if (refreshToken.isEmpty()) {
            return null;
        }
        RefreshToken entity = refreshToken.get();
        entity.setToken("refresh_" + UUID.randomUUID());
        entity.setExpireDate(expire);
        return repository.save(entity);

    }
}
