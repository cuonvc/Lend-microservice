package com.lender.authservice.service;

import com.lender.authservice.payload.request.LoginRequest;
import com.lender.authservice.payload.request.ProfileRequest;
import com.lender.authservice.payload.request.RegRequest;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<BaseResponse<UserResponse>> register(RegRequest request);

    ResponseEntity<BaseResponse<String>> login(LoginRequest request);

    ResponseEntity<BaseResponse<UserResponse>> editProfile(ProfileRequest request);
}
