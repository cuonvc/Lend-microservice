package com.lender.authservice.service.impl;

import com.lender.authservice.config.CustomerUserDetail;
import com.lender.authservice.config.SecurityConfiguration;
import com.lender.authservice.config.jwt.JwtTokenProvider;
import com.lender.authservice.entity.User;
import com.lender.authservice.mapper.UserMapper;
import com.lender.authservice.payload.request.LoginRequest;
import com.lender.authservice.payload.request.ProfileRequest;
import com.lender.authservice.payload.request.RegRequest;
import com.lender.authservice.payload.response.PageResponseUsers;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import com.lender.authservice.repository.UserRepository;
import com.lender.authservice.response.ResponseFactory;
import com.lender.authservice.service.UserService;
import com.lender.baseservice.exception.APIException;
import com.lender.baseservice.exception.ResourceNotFoundException;
import com.lender.baseservice.payload.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        CustomerUserDetail customerUserDetail = (CustomerUserDetail) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        User user = userRepository.findById(customerUserDetail.getId())
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
