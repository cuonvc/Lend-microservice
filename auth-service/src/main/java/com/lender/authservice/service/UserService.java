package com.lender.authservice.service;

import com.lender.authservice.payload.request.LoginRequest;
import com.lender.authservice.payload.request.ProfileRequest;
import com.lender.authservice.payload.request.RegRequest;
import com.lender.authservice.payload.request.TokenObjectRequest;
import com.lender.authservice.payload.response.PageResponseUsers;
import com.lender.authservice.payload.response.TokenObjectResponse;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<BaseResponse<UserResponse>> register(RegRequest request);

    ResponseEntity<BaseResponse<TokenObjectResponse>> login(LoginRequest request);

    ResponseEntity<BaseResponse<TokenObjectResponse>> renewAccessToken(TokenObjectRequest request);

    ResponseEntity<BaseResponse<UserResponse>> editProfile(ProfileRequest request);

    ResponseEntity<BaseResponse<UserResponse>> getById(String id);

    ResponseEntity<BaseResponse<PageResponseUsers>> getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir);
}
