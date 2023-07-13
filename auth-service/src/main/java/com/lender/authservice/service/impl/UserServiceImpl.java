package com.lender.authservice.service.impl;

import com.lender.authservice.config.CustomUserDetail;
import com.lender.authservice.config.jwt.JwtTokenProvider;
import com.lender.authservice.entity.RefreshToken;
import com.lender.authservice.entity.User;
import com.lender.authservice.mapper.UserMapper;
import com.lender.authservice.payload.request.*;
import com.lender.authservice.payload.response.PageResponseUsers;
import com.lender.authservice.payload.response.TokenObjectResponse;
import com.lender.authservice.repository.RefreshTokenRepository;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import com.lender.authservice.repository.UserRepository;
import com.lender.authservice.response.ResponseFactory;
import com.lender.authservice.service.TokenService;
import com.lender.authservice.service.UserService;
import com.lender.baseservice.exception.APIException;
import com.lender.baseservice.payload.request.FileObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final TokenService tokenService;
    private final UserMapper userMapper;
    private final ResponseFactory responseFactory;
    private final StreamBridge streamBridge;

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> register(RegRequest request) {
        Optional<User> entity = userRepository.findByEmail(request.getEmail());
        if (entity.isPresent()) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Account already existed", null);
        }

        User user = userMapper.requestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        tokenService.initRefreshToken(user);
        UserResponse response = userMapper.entityToResponse(saved);
        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<TokenObjectResponse>> login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST,
                    "User not found with email '" + request.getEmail() + "'", null);
        }

        if (!validPassword(request.getPassword(), user.get().getPassword())) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Password incorrect", null);
        }

        String accessToken = jwtTokenProvider.generateToken(request.getEmail());
        RefreshToken refreshToken = tokenService.generateTokenObject(user.get());
        TokenObjectResponse response = TokenObjectResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .refreshToken(refreshToken)
                .build();

        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<TokenObjectResponse>> renewAccessToken(TokenObjectRequest request) {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<RefreshToken> refreshToken = tokenRepository.getByUserIdAndToken(userDetail.getId(), request.getRefreshToken());

        if (refreshToken.isEmpty()) {
            return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Not happen in this case!", null);
        }

        if (refreshToken.get().getExpireDate().compareTo(new Date()) > 0) {
            return responseFactory.success("Success",
                    TokenObjectResponse.builder()
                            .accessToken(jwtTokenProvider.generateToken(userDetail.getUsername()))
                            .refreshToken(refreshToken.get())
                            .build());
        }

        return responseFactory.fail(HttpStatus.UNAUTHORIZED, "Refresh token has expired", null);
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
    public ResponseEntity<BaseResponse<UserResponse>> getById(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return responseFactory.fail(HttpStatus.NOT_FOUND, "User not found with id: " + userId, null);
        }

        return responseFactory.success("Success", userMapper.entityToResponse(user.get()));
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
    public ResponseEntity<BaseResponse<String>> changeAvatar(MultipartFile file) throws IOException {
        CustomUserDetail userDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message<FileObjectRequest> requestMessage = MessageBuilder
                .withPayload(FileObjectRequest.builder()
                        .field("avatarUrl")
                        .fileBytes(file.getBytes())
                        .build())
                .setHeader(KafkaHeaders.KEY, userDetail.getId().getBytes())
                .build();

        streamBridge.send("file-request", requestMessage);
        return responseFactory.success("Pending", "Saving image...");
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
}
