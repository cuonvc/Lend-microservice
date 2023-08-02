package com.lender.authservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lender.authservice.config.jwt.JwtTokenProvider;
import com.lender.authservice.entity.RefreshToken;
import com.lender.authservice.entity.User;
import com.lender.authservice.mapper.OAuthUserMapper;
import com.lender.authservice.mapper.TokenMapper;
import com.lender.authservice.payload.OAuthUserInfo;
import com.lender.authservice.payload.request.GithubRequestToken;
import com.lender.authservice.payload.response.GithubResponseToken;
import com.lender.authservice.payload.response.GithubResponseUser;
import com.lender.authservice.payload.response.GoogleResponseUser;
import com.lender.authservice.repository.RefreshTokenRepository;
import com.lender.authservice.repository.UserRepository;
import com.lender.authservice.service.OAuthService;
import com.lender.authservice.service.TokenService;
import com.lender.authserviceshare.payload.enumerate.Gender;
import com.lender.authserviceshare.payload.enumerate.Role;
import com.lender.authserviceshare.payload.enumerate.UserProvider;
import com.lender.authserviceshare.payload.response.TokenObjectResponse;
import com.lender.baseservice.constant.enumerate.Status;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.ResponseFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class OAuthController {

    private final OAuthService oAuthService;
    private final ResponseFactory responseFactory;

    @PostMapping("/validate")
    @Transactional
    public ResponseEntity<BaseResponse<TokenObjectResponse>> oAuthValidateToken(@RequestParam(name = "provider") String provider,
                                                                                @RequestParam(name = "token") String token) {
        log.info("triggerr - {} - {}", provider, token);

        provider = provider.toUpperCase();
        return switch (provider) {
            case "GOOGLE" -> oAuthService.validateGoogleToken(token);
            case "GITHUB" -> oAuthService.validateGithubCode(token);
            case "FACEBOOK" -> oAuthService.validateFacebookToken(token);
            default -> responseFactory.fail(HttpStatus.BAD_REQUEST, "Defaul case - unknown error", null);
        };
    }

}
