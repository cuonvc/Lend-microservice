package com.lender.authservice.service.impl;

import com.lender.authservice.config.SecurityConfiguration;
import com.lender.authservice.config.jwt.JwtTokenProvider;
import com.lender.authservice.entity.User;
import com.lender.authservice.mapper.UserMapper;
import com.lender.authservice.payload.request.UserRequest;
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
    public ResponseEntity<BaseResponse<UserResponse>> register(UserRequest request) {
        Optional<User> entity = userRepository.findByEmail(request.getEmail());
        if (entity.isPresent()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Account already existed!");
        }

        User user = userMapper.requestToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User saved = userRepository.save(user);
        UserResponse response = userMapper.entityToResponse(saved);
        return responseFactory.success("Success", response);
    }

    @Override
    public ResponseEntity<BaseResponse<String>> login(UserRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        if (!validPassword(request.getPassword(), user.getPassword())) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Password incorrect");
        }

//        Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );

//        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(request.getEmail());

        return responseFactory.success("Success", token);
    }

    private boolean validPassword(String rawPassword, String archivePassword) {
        return passwordEncoder.matches(rawPassword, archivePassword);
    }
}
