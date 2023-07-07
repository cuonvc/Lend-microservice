package com.lender.authservice.controller;

import com.lender.authservice.payload.request.UserRequest;
import com.lender.authservice.response.BaseResponse;
import com.lender.authservice.payload.response.UserResponse;
import com.lender.authservice.service.JwtService;
import com.lender.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BaseResponse<UserResponse>> signup(@RequestBody UserRequest userRequest) {
        return userService.register(userRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<String>> login(@RequestBody UserRequest userRequest) {
        return userService.login(userRequest);
    }
}
