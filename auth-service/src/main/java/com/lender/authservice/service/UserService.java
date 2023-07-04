package com.lender.authservice.service;

import com.lender.authservice.payload.request.UserRequest;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<BaseResponse<UserResponse>> register(UserRequest request);
}
