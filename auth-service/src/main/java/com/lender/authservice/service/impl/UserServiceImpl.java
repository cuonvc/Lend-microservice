package com.lender.authservice.service.impl;

import com.lender.authservice.entity.User;
import com.lender.authservice.mapper.UserMapper;
import com.lender.authservice.payload.request.UserRequest;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import com.lender.authservice.repository.UserRepository;
import com.lender.authservice.response.ResponseFactory;
import com.lender.authservice.service.UserService;
import com.lender.baseservice.exception.APIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ResponseFactory responseFactory;

    @Override
    public ResponseEntity<BaseResponse<UserResponse>> register(UserRequest request) {
        Optional<User> entity = userRepository.findByEmail(request.getEmail());
        if (!entity.isEmpty()) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Account already existed!");
        }

        request.setPassword(passwordEncoder.encode(request.getPassword()));
        User user = userMapper.requestToEntity(request);
        UserResponse response = userMapper.entityToResponse(userRepository.save(user));
        return responseFactory.success("Success", response);
    }

}
