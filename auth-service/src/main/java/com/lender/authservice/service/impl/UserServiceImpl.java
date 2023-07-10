package com.lender.authservice.service.impl;

import com.lender.authservice.config.SecurityConfiguration;
import com.lender.authservice.config.jwt.JwtTokenProvider;
import com.lender.authservice.entity.User;
import com.lender.authservice.mapper.UserMapper;
import com.lender.authservice.payload.request.LoginRequest;
import com.lender.authservice.payload.request.ProfileRequest;
import com.lender.authservice.payload.request.RegRequest;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import com.lender.authservice.repository.UserRepository;
import com.lender.authservice.response.ResponseFactory;
import com.lender.authservice.service.UserService;
import com.lender.baseservice.exception.APIException;
import com.lender.baseservice.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> register(RegRequest request) {
        Optional<User> entity = userRepository.findByEmail(request.getEmail());
        if (entity.isPresent()) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Account already existed", null);
        }

        User user = userMapper.requestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        UserResponse response = userMapper.entityToResponse(saved);
        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<String>> login(LoginRequest request) {
        Optional<User> user = userRepository.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST,
                    "User not found with email '" + request.getEmail() + "'", null);
        }

        if (!validPassword(request.getPassword(), user.get().getPassword())) {
            return responseFactory.fail(HttpStatus.BAD_REQUEST, "Password incorrect", null);
        }

//        Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.get().getEmail(), user.get().getPassword())
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(request.getEmail());

        return responseFactory.success("Success", token);
    }

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> editProfile(ProfileRequest request) {
        String role = String.valueOf(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().toList().get(0));
        return null;
    }

    private boolean validPassword(String rawPassword, String archivePassword) {
        return passwordEncoder.matches(rawPassword, archivePassword);
    }
}
