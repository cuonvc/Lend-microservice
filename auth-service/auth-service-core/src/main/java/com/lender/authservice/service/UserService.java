package com.lender.authservice.service;

import com.lender.authservice.payload.request.PasswordChangeRequest;
import com.lender.authservice.payload.request.RenewPasswordRequest;
//import com.lender.authservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.authserviceshare.payload.request.LoginRequest;
import com.lender.authserviceshare.payload.request.ProfileRequest;
import com.lender.authserviceshare.payload.request.RegRequest;
import com.lender.authserviceshare.payload.request.TokenObjectRequest;
import com.lender.authserviceshare.payload.response.PageResponseUsers;
import com.lender.authserviceshare.payload.response.TokenObjectResponse;
import com.lender.authserviceshare.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    ResponseEntity<BaseResponse<String>> register(RegRequest request);

    ResponseEntity<BaseResponse<UserResponse>> validate(RegRequest request, String key);

    ResponseEntity<BaseResponse<TokenObjectResponse>> login(LoginRequest request);

    ResponseEntity<BaseResponse<String>> logout();

    ResponseEntity<BaseResponse<TokenObjectResponse>> renewAccessToken(TokenObjectRequest request);

    ResponseEntity<BaseResponse<String>> forgotPasswordRequest(String email);

    ResponseEntity<BaseResponse<String>> renewPassword(RenewPasswordRequest request);

    ResponseEntity<BaseResponse<UserResponse>> editProfile(ProfileRequest request);

    ResponseEntity<BaseResponse<String>> changePassword(PasswordChangeRequest request);

    ResponseEntity<BaseResponse<UserResponse>> getById(String id);

    ResponseEntity<BaseResponse<PageResponseUsers>> getAll(Integer pageNo, Integer pageSize, String sortBy, String sortDir);

    ResponseEntity<BaseResponse<String>> uploadAvatar(MultipartFile file) throws IOException;

    void saveChangeImage(String userId, String field, String path);

    ResponseEntity<BaseResponse<String>> assignRole(String role, String userId);
}
