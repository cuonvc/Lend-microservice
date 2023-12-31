package com.lend.authservice.controller;

import com.lend.authservice.exception.ResourceNotFoundException;
import com.lend.authservice.repository.UserRepository;
import com.lend.authservice.config.CustomUserDetail;
import com.lend.authservice.config.CustomUserDetailService;
import com.lend.authservice.config.jwt.JwtTokenProvider;
import com.lend.authservice.entity.User;
import com.lend.authservice.payload.response.CustomUserDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/internal")
@RequiredArgsConstructor
public class InternalController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailService customUserDetailService;
    private final UserRepository userRepository;

    @GetMapping("/check")
    public ResponseEntity<CustomUserDetailResponse> validateToken(@RequestParam("token") String token) {
        if (jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmailFromToken(token);
            UserDetails userDetails = customUserDetailService.loadUserByUsername(email);
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
            CustomUserDetailResponse userDetailResponse = CustomUserDetailResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .grantedAuthorities(List.of(user.getRole().toString()))
                    .build();

            return ResponseEntity.ok(userDetailResponse);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    private UsernamePasswordAuthenticationToken buildAuthenticationToken(CustomUserDetail userDetails, List<GrantedAuthority> authorityList) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = authorityList.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .toList();

        return new UsernamePasswordAuthenticationToken(userDetails, null, simpleGrantedAuthorities);
    }
}
