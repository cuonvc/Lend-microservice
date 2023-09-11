package com.lend.authservice.service.impl;

import com.lend.authservice.config.jwt.JwtTokenProvider;
import com.lend.authservice.entity.RefreshToken;
import com.lend.authservice.entity.User;
import com.lend.authservice.mapper.OAuthUserMapper;
import com.lend.authservice.mapper.TokenMapper;
import com.lend.authservice.payload.OAuthUserInfo;
import com.lend.authservice.payload.response.GithubResponseToken;
import com.lend.authservice.payload.response.GithubResponseUser;
import com.lend.authservice.payload.response.GoogleResponseUser;
import com.lend.authservice.repository.RefreshTokenRepository;
import com.lend.authservice.repository.UserRepository;
import com.lend.authservice.service.OAuthService;
import com.lend.authservice.service.TokenService;
import com.lend.authserviceshare.payload.enumerate.Gender;
import com.lend.authserviceshare.payload.enumerate.Role;
import com.lend.authserviceshare.payload.enumerate.UserProvider;
import com.lend.authserviceshare.payload.response.TokenObjectResponse;
import com.lend.baseservice.constant.enumerate.Status;
import com.lend.baseservice.payload.response.BaseResponse;
import com.lend.baseservice.payload.response.ResponseFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthServiceImpl implements OAuthService {

    private static final String GOOGLE_API_INFO = "https://www.googleapis.com/oauth2/v3/userinfo";
    private static final String GITHUB_API_TOKEN = "https://github.com/login/oauth/access_token";
    private static final String GITHUB_API_INFO = "https://api.github.com/user";

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String githubClientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String githubClientSecret;

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final RefreshTokenRepository tokenRepository;
    private final EntityManager entityManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenMapper tokenMapper;
    private final ResponseFactory responseFactory;
    private final OAuthUserMapper oAuthUserMapper;

    @Override
    public ResponseEntity<BaseResponse<TokenObjectResponse>> validateGoogleToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GoogleResponseUser> response = restTemplate.exchange(GOOGLE_API_INFO, HttpMethod.GET, entity, GoogleResponseUser.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                GoogleResponseUser data = response.getBody();
                log.info("response_user_info - {}", data);
                return saveUser(oAuthUserMapper.toUserInfo(data), UserProvider.GOOGLE);

            } else {
                log.info("Failure...");
                return responseFactory.fail(HttpStatus.BAD_REQUEST, "Đăng nhập thất bại...", null);
            }
        } catch (HttpClientErrorException e) {
            log.error("Failed request in try-catch - {}", e.getMessage());
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Đăng nhập thất bại...", null);
        }
    }

    @Override
    public ResponseEntity<BaseResponse<TokenObjectResponse>> validateGithubCode(String code) {
        log.info("Logging github - {}", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(GITHUB_API_TOKEN)
                .queryParam("client_id", githubClientId)
                .queryParam("client_secret", githubClientSecret)
                .queryParam("code", code);

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<GithubResponseToken> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                request,
                GithubResponseToken.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Token - {}", response.getBody().getAccess_token());
            GithubResponseUser userInfo = getGithubUserInfo(response.getBody());
            return saveUser(oAuthUserMapper.toUserInfo(userInfo), UserProvider.GITHUB);

        } else {
            log.info("Failure...");
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Đăng nhập thất bại...", null);
        }
    }

    @Override
    public ResponseEntity<BaseResponse<TokenObjectResponse>> validateFacebookToken(String code) {
        log.info("Logging facebook - {}", code);
        return null;
    }

    private GithubResponseUser getGithubUserInfo(GithubResponseToken responseToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, responseToken.getToken_type() + " " + responseToken.getAccess_token());
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GithubResponseUser> response = restTemplate.exchange(GITHUB_API_INFO, HttpMethod.GET, httpEntity, GithubResponseUser.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                GithubResponseUser data = response.getBody();
                log.info("response_user_info - {}", data);
                return data;
            } else {
                log.info("Failure...");
            }
        } catch (HttpClientErrorException e) {
            log.error("Failed request in try-catch - {}", e.getMessage());
        }

        return null;
    }

    private ResponseEntity<BaseResponse<TokenObjectResponse>> saveUser(OAuthUserInfo userInfo, UserProvider provider) {
        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElse(User.builder()
                        .firstName(userInfo.getFirstName())
                        .lastName(userInfo.getLastName())
                        .email(userInfo.getEmail())
                        .avatarUrl(userInfo.getAvatarUrl())
                        .createdDate(LocalDateTime.now())
                        .role(Role.USER)
                        .provider(provider)
                        .status(Status.ACTIVE)
                        .gender(Gender.UNDEFINE)
                        .build());
        user = userRepository.save(user);

        RefreshToken refreshToken = tokenRepository.getByUserId(user.getId())
                .orElse(RefreshToken.builder()
                        .user(user)
                        .build());
        entityManager.persist(refreshToken);
        refreshToken = tokenService.generateTokenObject(user);
        entityManager.merge(refreshToken);
        String accessToken = jwtTokenProvider.generateToken(user.getEmail());

        TokenObjectResponse responseObject = TokenObjectResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .refreshToken(tokenMapper.mapToDto(refreshToken))
                .build();

        return responseFactory.success("Success", responseObject);
    }
}
