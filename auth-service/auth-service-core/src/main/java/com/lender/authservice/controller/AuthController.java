package com.lender.authservice.controller;

import com.lender.authservice.response.BaseResponse;
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

    @GetMapping("/valid")  //API valid token for gateway service
    public boolean validateToken(@RequestParam("token") String token) {
        return tokenService.validateAccessToken(token);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<String>> signup(@Valid @RequestBody RegRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<TokenObjectResponse>> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/token/renew")
    public ResponseEntity<BaseResponse<TokenObjectResponse>> renewAccessToken(@RequestBody TokenObjectRequest request) {
        return userService.renewAccessToken(request);
    }

    @PutMapping("/account/edit")
    public ResponseEntity<BaseResponse<UserResponse>> editProfile(@Valid @RequestBody ProfileRequest request) {
        return userService.editProfile(request);
    }

    @GetMapping("/account/{userId}")
    public ResponseEntity<BaseResponse<UserResponse>> getProfile(@PathVariable(name = "userId") String id) {
        return userService.getById(id);
    }

    @GetMapping("/admin/account-list")
    public ResponseEntity<BaseResponse<PageResponseUsers>> getAllAccount(@RequestParam(value = "pageNo",
                                                                                  defaultValue = PageConstant.PAGE_NO, required = false) Integer pageNo,
                                                                         @RequestParam(value = "pageSize",
                                                                                  defaultValue = PageConstant.PAGE_SIZE, required = false) Integer pageSize,
                                                                         @RequestParam(value = "sortBy",
                                                                                  defaultValue = PageConstant.SORT_BY, required = false) String sortBy,
                                                                         @RequestParam(value = "sortDir",
                                                                                  defaultValue = PageConstant.SORT_DIR, required = false) String sortDir) {
        return userService.getAll(pageNo, pageSize, sortBy, sortDir);
    }

    @PutMapping("/account/avatar")
    public ResponseEntity<BaseResponse<String>> uploadAvatar(@RequestPart(name = "image") MultipartFile file) throws IOException {
        return userService.uploadAvatar(file);
    }
}