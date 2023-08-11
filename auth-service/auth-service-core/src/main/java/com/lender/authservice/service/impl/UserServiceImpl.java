package com.lender.authservice.service.impl;

import com.lender.authservice.config.CustomUserDetail;
import com.lender.authservice.config.jwt.JwtTokenProvider;
import com.lender.authservice.entity.RefreshToken;
import com.lender.authservice.entity.User;
import com.lender.authservice.exception.APIException;
import com.lender.authservice.exception.ResourceNotFoundException;
import com.lender.authservice.mapper.TokenMapper;
import com.lender.authservice.mapper.UserMapper;
import com.lender.authservice.payload.request.PasswordChangeRequest;
import com.lender.authservice.payload.request.RenewPasswordRequest;
import com.lender.authservice.repository.RefreshTokenRepository;
//import com.lender.authservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.authservice.repository.UserRepository;
//import com.lender.authservice.payload.response.ResponseFactory;
import com.lender.baseservice.payload.response.ResponseFactory;
import com.lender.authservice.service.TokenService;
import com.lender.authservice.service.UserService;
import com.lender.authserviceshare.payload.enumerate.Role;
import com.lender.authserviceshare.payload.request.LoginRequest;
import com.lender.authserviceshare.payload.request.ProfileRequest;
import com.lender.authserviceshare.payload.request.RegRequest;
import com.lender.authserviceshare.payload.request.TokenObjectRequest;
import com.lender.authserviceshare.payload.response.PageResponseUsers;
import com.lender.authserviceshare.payload.response.TokenObjectResponse;
import com.lender.authserviceshare.payload.response.UserResponse;
import com.lender.baseservice.payload.request.FileObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String AVATAR = "avatarUrl";
    private static final String COVER = "";

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final TokenMapper tokenMapper;
    private final ResponseFactory responseFactory;
    private final StreamBridge streamBridge;
    private final RedisTemplate<RegRequest, String> redisTemplateObject;
    private final RedisTemplate<String, String> redisTemplateString;

    @Override
    public ResponseEntity<BaseResponse<String>> register(RegRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Account already existed", null);
        }

        String activeCode = String.valueOf(new Random().nextInt(900000) + 100000);
        redisTemplateObject.opsForValue().set(request, activeCode);
        redisTemplateObject.expire(request, 5, TimeUnit.MINUTES);
        log.info("Redis cached - {}", redisTemplateObject.opsForValue().get(request));

        Message<String> message = MessageBuilder.withPayload(activeCode)
                .setHeader(KafkaHeaders.KEY, request.getEmail().getBytes())
                .build();
        streamBridge.send("email-active", message);
        return responseFactory.success("An OTP code send to your email, please check now", request.getEmail());


//        User user = userMapper.requestToEntity(request);
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        User saved = userRepository.save(user);
//        tokenService.initRefreshToken(user);
//        UserResponse response = userMapper.entityToResponse(saved);
//        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> validate(RegRequest request, String code) {
        String activeCode = redisTemplateObject.opsForValue().get(request);
        if (activeCode == null) {
            return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Activation code has expired!", null);
        }

        if (!activeCode.equals(code)) {
            return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Incorrect activation code", null);
        }

        User user = userMapper.requestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        tokenService.initRefreshToken(user);
        UserResponse response = userMapper.entityToResponse(saved);
        redisTemplateObject.delete(request);
        return responseFactory.success("Account activated successfully!", response);
    }

    @Override
    public ResponseEntity<BaseResponse<TokenObjectResponse>> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        if (!validPassword(request.getPassword(), user.getPassword())) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Password incorrect", null);
        }

        String accessToken = jwtTokenProvider.generateToken(request.getEmail());
        RefreshToken refreshToken = tokenService.generateTokenObject(user);
        TokenObjectResponse response = TokenObjectResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .refreshToken(tokenMapper.mapToDto(refreshToken))
                .build();

        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<String>> logout() {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        tokenService.clearToken(userDetail.getId());
        return responseFactory.success("Logged out", "Success");
    }

    @Override
    public ResponseEntity<BaseResponse<TokenObjectResponse>> renewAccessToken(TokenObjectRequest request) {

        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        RefreshToken refreshToken = tokenRepository.getByUserIdAndToken(userDetail.getId(), request.getRefreshToken())
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "You are logged out"));

        if (refreshToken.getExpireDate().compareTo(new Date()) > 0) {
            return responseFactory.success("Success",
                    TokenObjectResponse.builder()
                            .accessToken(jwtTokenProvider.generateToken(userDetail.getUsername()))
                            .refreshToken(tokenMapper.mapToDto(refreshToken))
                            .build());
        }

        return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Refresh token has expired", null);
    }

    @Override
    public ResponseEntity<BaseResponse<String>> forgotPasswordRequest(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        String activeCode = String.valueOf(new Random().nextInt(900000) + 100000);
        redisTemplateString.opsForValue().set(activeCode, email);
        redisTemplateString.expire(email, 10, TimeUnit.MINUTES);
        log.info("Redis cached string - {}", redisTemplateString.opsForValue().get(activeCode));

        Message<String> message = MessageBuilder.withPayload(activeCode)
                .setHeader(KafkaHeaders.KEY, email.getBytes())
                .build();
        streamBridge.send("password-forgot", message);
        return responseFactory.success("An OTP code send to your email, please check now", email);
    }

    @Override
    public ResponseEntity<BaseResponse<String>> renewPassword(RenewPasswordRequest request) {

        return (ResponseEntity<BaseResponse<String>>) Optional.ofNullable(redisTemplateString.opsForValue().get(request.getCode()))
                .map(email -> {
                    if (!request.getNewPassword().equals(request.getRetypePassword())) {
                        return responseFactory.fail(HttpStatus.BAD_REQUEST, "Password do not matches", null);
                    }

                    User user = userRepository.findByEmail(email).get();
                    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                    userRepository.save(user);
                    redisTemplateString.delete(request.getCode());
                    return responseFactory.success("Redirect to login with new password", email);
                })
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "OTP code incorrect"));
    }

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> editProfile(ProfileRequest request) {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(customUserDetail.getId())
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "A unknown error"));  //not happen

        user = userMapper.profileToEntity(request, user);
        user.setModifiedDate(LocalDateTime.now());
        UserResponse response = userMapper.entityToResponse(userRepository.save(user));
        return responseFactory.success("Update successfully!", response);
    }

    @Override
    public ResponseEntity<BaseResponse<String>> changePassword(PasswordChangeRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(userDetail.getId())
                .orElseThrow(() -> new APIException(HttpStatus.UNAUTHORIZED, "Unknown error"));

        if (!request.getNewPassword().equals(request.getRetypePassword())) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Password do not match", null);
        }

        if (!validPassword(request.getOldPassword(), user.getPassword())) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Incorrect password", null);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return responseFactory.success("Password has been changed", "Success");
    }

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> getById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return responseFactory.success("Success", userMapper.entityToResponse(user));
    }

    @Override
    public ResponseEntity<BaseResponse<PageResponseUsers>> getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findAll(pageable);
        PageResponseUsers pageResponse = paging(users);
        return responseFactory.success("Success", pageResponse);
    }

    @Override
    public ResponseEntity<BaseResponse<String>> uploadAvatar(MultipartFile file) throws IOException {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message<FileObjectRequest> requestMessage = MessageBuilder
                .withPayload(FileObjectRequest.builder()
                        .field(AVATAR)
                        .fileBytes(file.getBytes())
                        .build())
                .setHeader(KafkaHeaders.KEY, userDetail.getId().getBytes())
                .build();

        streamBridge.send("user-avatar-request", requestMessage);
        return responseFactory.success("Pending", "Saving image...");
    }

    @Override
    public void saveChangeImage(String userId, String field, String path) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        switch (field) {
            case AVATAR -> user.setAvatarUrl(path);
            case COVER -> user.setCoverUrl(path);
            default -> throw new RuntimeException();
        }

        user.setModifiedDate(LocalDateTime.now());
        userRepository.save(user);
    }

    private PageResponseUsers paging(Page<User> users) {
        List<UserResponse> userList = users.getContent()
                .stream().map(userMapper::entityToResponse)
                .toList();

        return (PageResponseUsers) PageResponseUsers.builder()
                .pageNo(users.getNumber())
                .pageSize(userList.size())
                .content(userList)
                .totalPages(users.getTotalPages())
                .totalItems((int) users.getTotalElements())
                .last(users.isLast())
                .build();
    }

    private boolean validPassword(String rawPassword, String archivePassword) {
        return passwordEncoder.matches(rawPassword, archivePassword);
    }

    @Override
    public ResponseEntity<BaseResponse<String>> assignRole(String role, String userId) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Trigger - {}", "");

        boolean roleMatch = Stream.of(Role.values())
                .map(eRole -> eRole.name())
                .toList()
                .contains(role);
        if (!roleMatch) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Role do not match", null);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        int authorRole = Role.valueOf(String.valueOf(userDetail.getAuthorities().stream().toList().get(0))).getNumVal();
        if (authorRole < Role.valueOf(role).getNumVal()) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Can not assign with role higher", null);
        }

        user.setRole(Role.valueOf(role));
        userRepository.save(user);

        return responseFactory.success("Success", role);
    }
}
