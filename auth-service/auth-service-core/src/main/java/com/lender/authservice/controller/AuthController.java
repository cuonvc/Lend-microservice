package com.lender.authservice.controller;

import com.lender.authservice.payload.request.PasswordChangeRequest;
import com.lender.authservice.payload.request.RenewPasswordRequest;
//import com.lender.authservice.payload.response.BaseResponse;
import com.lender.baseservice.payload.response.BaseResponse;
import com.lender.authservice.service.TokenService;
import com.lender.authservice.service.UserService;
import com.lender.authserviceshare.payload.request.LoginRequest;
import com.lender.authserviceshare.payload.request.ProfileRequest;
import com.lender.authserviceshare.payload.request.RegRequest;
import com.lender.authserviceshare.payload.request.TokenObjectRequest;
import com.lender.authserviceshare.payload.response.PageResponseUsers;
import com.lender.authserviceshare.payload.response.TokenObjectResponse;
import com.lender.authserviceshare.payload.response.UserResponse;
import com.lender.baseservice.constant.PageConstant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final TokenService tokenService;

    @GetMapping("/gateway/valid")  //API valid token for gateway service
    public boolean validateToken(@RequestParam("token") String token) {
        return tokenService.validateAccessToken(token);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<String>> signup(@Valid @RequestBody RegRequest request) {
        return userService.register(request);
    }

    @PostMapping("/active-account")
    //client caching RegRequest to browser when user click sign up
    public ResponseEntity<BaseResponse<UserResponse>> active(@RequestBody RegRequest request, @RequestParam("active_key") String key) {
        return userService.validate(request, key);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<TokenObjectResponse>> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<String>> logout() {
        return userService.logout();
    }

    @GetMapping("/token/renew")
    public ResponseEntity<BaseResponse<TokenObjectResponse>> renewAccessToken(@RequestBody TokenObjectRequest request) {
        return userService.renewAccessToken(request);
    }

}
