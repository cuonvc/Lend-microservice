package com.lender.authservice.controller;

import com.lender.authservice.payload.request.LoginRequest;
import com.lender.authservice.payload.request.ProfileRequest;
import com.lender.authservice.payload.request.RegRequest;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import com.lender.authservice.service.JwtService;
import com.lender.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final JwtService jwtService;

    @GetMapping("/valid")
    public boolean validateToken(@RequestParam("token") String token) {
        return jwtService.validateToken(token);

    }

    @PostMapping("/sign-up")
    public ResponseEntity<BaseResponse<UserResponse>> signup(@Valid @RequestBody RegRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<String>> login(@Valid @RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PutMapping("/account/edit")
    public ResponseEntity<BaseResponse<UserResponse>> editProfile(@Valid @RequestBody ProfileRequest request) {
        return userService.editProfile(request);
    }
}
